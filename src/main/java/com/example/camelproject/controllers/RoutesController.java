package com.example.camelproject.controllers;

import com.example.camelproject.configs.ActivemqProperties;
import com.example.camelproject.models.Person;
import com.example.camelproject.sevices.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class RoutesController {

    private final JmsTemplate jmsTemplate;

    private final PersonService personService;

    private final ActivemqProperties activemqProperties;


    /**
     * Adds message in Activemq queue.
     *
     * @param person json value.
     * @Example: {
     * "name":"Nikita",
     * "age":12,
     * "email":"Nikita@gmail.com"
     * }
     */
    @PostMapping("/activemq")
    public void send(@RequestBody(required = false) Person person) {
        jmsTemplate.convertAndSend(activemqProperties.getCamelQueueName(), person);
    }


    /**
     * Prints user's info from Rest Route.
     *
     * @param person value from rest route
     */
    @PatchMapping(value = "/user")
    public void getUserInfo(@RequestBody Person person) {
        log.info("Got value from rest route: {}", person);
    }

    /**
     * Gets user from database and prints it in console.
     * Used by Timer route.
     *
     * @param id random user's id. Used by Timer route.
     */
    @GetMapping("/user/{id}")
    public void getRandomUser(@PathVariable int id) {
        log.info("Got random user from Timer route with body: " + personService.getPersonById(id));
    }


    /**
     * Saves user and prints it in console.
     *
     * @param person person to be saved
     */
    @PostMapping("/save/user")
    public void getUserFromActivemq(@RequestBody Person person) {
        log.info("Got person from Activemq: {}", person);
        personService.savePerson(person);
    }


}
