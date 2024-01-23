package com.example.camelproject.routes;

import com.example.camelproject.configs.ActivemqProperties;
import com.example.camelproject.sevices.PersonService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class ActivemqRoute extends RouteBuilder {

    private final PersonService personService;

    private final ActivemqProperties activemqProperties;

    /**
     * Route, that is listening to Activemq queue.
     * When new message is added it saves it into database and then sends to http://localhost:8080/savedUser in header "Person".
     *
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        from("activemq:" + activemqProperties.getCamelQueueName())
                .onException(RuntimeException.class)
                    .setBody(simple("${exception.message}"))
                    .log(LoggingLevel.WARN, "${body}")
                    .handled(true)
                .end()
                .routeId("Activemq route")
                .log(LoggingLevel.INFO, "Got json from Activemq: ${body}")
                .process(personService::savePersonAndSetItToExchange)
                .to("http://localhost:8080/savedUser?httpMethod=GET");
    }
}
