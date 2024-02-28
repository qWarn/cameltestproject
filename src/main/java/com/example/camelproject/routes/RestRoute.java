package com.example.camelproject.routes;

import com.example.camelproject.models.Person;
import com.example.camelproject.sevices.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestRoute extends RouteBuilder {

    private final PersonService personService;
    private final JacksonDataFormat format = new JacksonDataFormat(Person.class);

    /**
     * Route, which handles requests on http://localhost:8080/service/route consumes application/json.
     * Then sends body to http://localhost:8080/user.
     *
     * @throws Exception
     * @Example:{ "id":1,
     * "name":"Nikita",
     * "age":12,
     * "email":"Nikita@gmail.com"
     * }
     */
    @Override
    public void configure() throws Exception {
        from("rest:get:route?host=localhost:8080&consumes=application/json")
                .routeId("Rest route")
                .doTry()
                .unmarshal(format)
                .process(personService::setPersonToExchange)
                .marshal(format)
                .setHeader(Exchange.HTTP_METHOD, simple("PATCH"))
                .to("http://localhost:8080/user?bridgeEndpoint=true")
                .doCatch(JsonProcessingException.class)
                .log(LoggingLevel.ERROR, "Invalid body ${body}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setBody(simple("Invalid body ${body}"))
                .doCatch(Exception.class)
                .setBody(simple("${exception.message}"))
                .log(LoggingLevel.ERROR, "${exception.message}")
                .end();
    }
}
