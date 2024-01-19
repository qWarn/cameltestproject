package com.example.camelproject.routes;

import com.example.camelproject.configs.ActivemqProperties;
import com.example.camelproject.models.Person;
import com.example.camelproject.sevices.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivemqRoute extends RouteBuilder {


    private final ObjectMapper objectMapper;

    private final PersonService personService;

    private final ActivemqProperties activemqProperties;

    /**
     * Route, that is listening to Activemq queue.
     * When new message is added it saves it into database and then sends to http://localhost:8080/savedUser in header "Person".
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        from("activemq:" + activemqProperties.getCamelQueueName())
                .routeId("Activemq route")
                .log(LoggingLevel.INFO, "Got json from Activemq: ${body}")
                    .process(exchange -> {
                        Person person = personService.savePerson(
                                objectMapper.readValue(exchange.getIn().getBody(String.class), Person.class));
                        exchange.getMessage().setHeader("Person", objectMapper.writeValueAsString(person));
                    })
                .onException(DataIntegrityViolationException.class)
                    .process(exchange -> {
                        RuntimeException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                                DataIntegrityViolationException.class);
                        exchange.getIn().setBody(exception.getCause());
                    })
                    .log(LoggingLevel.ERROR, "${body}")
                    .handled(true)
                .end()
                .to("http://localhost:8080/savedUser?httpMethod=GET");

    }
}
