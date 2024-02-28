package com.example.camelproject.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class HttpOperationFailedProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        final HttpOperationFailedException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
        exchange.getIn().setBody(exception.getResponseBody());
    }
}
