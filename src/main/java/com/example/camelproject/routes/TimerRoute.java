package com.example.camelproject.routes;

import com.example.camelproject.sevices.PersonService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class TimerRoute extends RouteBuilder {

    private final PersonService personService;
    private final Random random;

    /**
     * Timer route, which every minute sends request to http://localhost:8080/random/{id} with random userId
     * (Starts working after 10 seconds. Change delay if you want).
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        from("timer:RandomPersonTimer?delay=10000&period=60000")
                .routeId("Timer route")
                    .process(exchange ->
                            exchange.getMessage()
                                    .setHeader("userId", random.nextLong(personService.countAllPeople()) + 1))
                .toD("http://localhost:8080/random/${header.userId}?httpMethod=GET")
                .onException(HttpOperationFailedException.class)
                    .log(LoggingLevel.ERROR, "User with id ${header.userId} doesn't exist").handled(true);
    }
}
