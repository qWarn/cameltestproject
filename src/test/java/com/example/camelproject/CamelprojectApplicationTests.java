package com.example.camelproject;

import com.example.camelproject.controllers.RoutesController;
import com.example.camelproject.sevices.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.springframework.util.Assert.*;

@SpringBootTest
class CamelprojectApplicationTests {

    @Autowired
    private PersonService personService;

    @Autowired
    private RoutesController routesController;

    @Test
    void contextLoads() {
        notNull(personService, "Service is null");
        notNull(routesController, "Controller is null");
    }

}
