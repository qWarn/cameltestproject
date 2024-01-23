package com.example.camelproject.routes;

import com.example.camelproject.sevices.PersonService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimerRoute extends RouteBuilder {

    private final PersonService personService;

    /**
     * Timer route, which every minute sends request to http://localhost:8080/random/{id} with random userId
     * (Starts working after 10 seconds. Change delay if you want).
     *
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        from("timer:RandomPersonTimer?delay=10000&period=60000")
                .onException(HttpOperationFailedException.class)
                    .log(LoggingLevel.ERROR, "User with id ${header.userId} doesn't exist")
                    .handled(true)
                .end()
                .routeId("Timer route")
                    .process(personService::getRandomIdAndSetItToExchange)
                .toD("http://localhost:8080/random/${header.userId}?httpMethod=GET");
    }
}

