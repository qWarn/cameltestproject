package com.example.camelproject.controllers;

import com.example.camelproject.configs.ActivemqProperties;
import com.example.camelproject.models.Person;
import com.example.camelproject.sevices.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoutesController {

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper;

    private final PersonService personService;

    private final ActivemqProperties activemqProperties;


    /**
     * Adds message in Activemq queue.
     * @param person json value.
     * @Example: {
     * "name":"Nikita",
     * "age":12,
     * "email":"Nikita@gmail.com"
     * }
     */
    @PostMapping("/sendMessageToActiveMQ")
    public void send(@RequestBody(required = false) Person person) throws JsonProcessingException { //Don't use dto because it's test project
        jmsTemplate.convertAndSend(activemqProperties.getCamelQueueName(), objectMapper.writeValueAsString(person));
    }


    /**
     * Prints user's info from Rest Route.
     * @param userId not implemented (only for practise)
     * @param request request, where I retrieve header, which contains body from Rest route (with @ResponseBody that doesn't work)
     * @throws JsonProcessingException
     *
     */
    @GetMapping("/user")
    public void getUserInfo(@RequestParam int userId, HttpServletRequest request) throws JsonProcessingException {
        log.info("Got user from rest route with body: " +
                objectMapper.readValue(request.getHeader("Person"), Person.class));
    }

    /**
     * Gets user from database and prints it in console.
     * Used by Timer route.
     * @param id random user's id. Used by Timer route.
     */
    @GetMapping("/random/{id}")
    public void getRandomUser(@PathVariable int id) {
        log.info("Got random user from Timer route with body: " + personService.getPersonById(id));
    }


    /**
     * Gets saved user from header and prints it in console.
     * Used by Activemq route.
     * @param request request, where I retrieve header, which contains body from Activemq route (with @ResponseBody that doesn't work)
     * @throws JsonProcessingException
     */
    @GetMapping("/savedUser")
    public void getUserFromActivemq(HttpServletRequest request) throws JsonProcessingException {
        log.info("Got user from Activemq " +
                objectMapper.readValue(request.getHeader("Person"), Person.class));
    }



}
