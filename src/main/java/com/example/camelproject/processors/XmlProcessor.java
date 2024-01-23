package com.example.camelproject.processors;

import com.example.camelproject.sevices.PersonService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class XmlProcessor implements Processor {

    private final PersonService personService;

    @Override
    public void process(Exchange exchange) throws Exception {
          personService.savePersonAndSetItToExchange(exchange);
    }

}
