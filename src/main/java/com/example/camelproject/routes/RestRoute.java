package com.example.camelproject.routes;

import com.example.camelproject.models.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestRoute extends RouteBuilder {

    private final ObjectMapper objectMapper;

    /**
     * Route, which handles requests on http://localhost:8080/service/restRoute consumes application/json.
     * Then sends body to http://localhost:8080/user?userId=id.
     * @Example:{
     *      "id":1,
     *      "name":"Nikita",
     *      "age":12,
     *      "email":"Nikita@gmail.com"
     *      }
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        from("rest:get:restRoute?host=localhost:8080&consumes=application/json")
                .routeId("Rest route")
                .log(LoggingLevel.INFO, "Got body: ${body}")
                .doTry()
                    .process(exchange -> {
                        String body = exchange.getIn().getBody(String.class);
                        exchange.getIn().setHeader("userId",
                                objectMapper.readValue(body, Person.class).getId());
                        exchange.getMessage().setHeader("Person", body);
                    })
                    .toD("http://localhost:8080/user?userId=${header.userId}&bridgeEndpoint=true&httpMethod=GET")
                    .setBody(constant("Success"))
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
