package com.example.camelproject.routes;

import com.example.camelproject.processors.HttpOperationFailedProcessor;
import com.example.camelproject.processors.TimerRouteProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimerRoute extends RouteBuilder {

    private final TimerRouteProcessor timerRouteProcessor;
    private final HttpOperationFailedProcessor httpOperationFailedProcessor;

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
                .process(httpOperationFailedProcessor)
                .log(LoggingLevel.ERROR, "Caught HttpOperationFailedException with body: ${body}")
                .handled(true)
                .end()
                .routeId("Timer route")
                .process(timerRouteProcessor)
                .toD("http://localhost:8080/user/${header.userId}?httpMethod=GET");
    }
}

