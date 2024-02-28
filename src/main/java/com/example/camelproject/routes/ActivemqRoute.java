package com.example.camelproject.routes;

import com.example.camelproject.configs.ActivemqProperties;
import com.example.camelproject.models.Person;
import com.example.camelproject.processors.HttpOperationFailedProcessor;
import com.example.camelproject.sevices.PersonService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.http.base.HttpOperationFailedException;

@RequiredArgsConstructor
public class ActivemqRoute extends RouteBuilder {

    private final PersonService personService;
    private final ActivemqProperties activemqProperties;
    private final JacksonDataFormat format = new JacksonDataFormat(Person.class);
    private final HttpOperationFailedProcessor httpOperationFailedProcessor;


    /**
     * Route, that is listening to Activemq queue.
     * When new message is added it sends it to http://localhost:8080/save/user.
     *
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        from("activemq:" + activemqProperties.getCamelQueueName())
                .onException(HttpOperationFailedException.class)
                .process(httpOperationFailedProcessor)
                .log(LoggingLevel.ERROR, "Caught HttpOperationFailedException with body: ${body}")
                .handled(true)
                .end()
                .onException(Exception.class)
                .setBody(simple("${exception.message}"))
                .log(LoggingLevel.ERROR, "Exception due proceeding Activemq route: ${body}")
                .handled(true)
                .end()
                .routeId("Activemq route")
                .log(LoggingLevel.INFO, "Got json from Activemq: ${body}")
                .process(personService::setPersonToExchange)
                .marshal(format)
                .to("http://localhost:8080/save/user?httpMethod=POST");
    }
}
