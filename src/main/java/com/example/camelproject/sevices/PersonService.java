package com.example.camelproject.sevices;

import com.example.camelproject.exceptions.EmailIsAlreadyUsedException;
import com.example.camelproject.exceptions.InvalidBodyException;
import com.example.camelproject.exceptions.PersonNotFoundException;
import com.example.camelproject.models.Person;
import com.example.camelproject.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    private final ObjectMapper objectMapper;

    private final Random random;

    public long countAllPeople() {
        return personRepository.count();
    }

    public Person getPersonById(int id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("Person with id " + id + " not found"));
    }

    @Transactional
    public Person savePerson(Person person) throws EmailIsAlreadyUsedException {
        if (personRepository.findByEmail(person.getEmail()).isPresent()) {
            throw new EmailIsAlreadyUsedException("Person with email " + person.getEmail() + " already exists");
        }
        return personRepository.save(person);
    }

    @Transactional
    public void savePersonAndSetItToExchange(Exchange exchange) throws InvalidBodyException {
        String personJSON = exchange.getIn().getBody(String.class);

        try {
            Person person = savePerson(objectMapper.readValue(personJSON, Person.class));
            setPersonHeader(exchange, objectMapper.writeValueAsString(person));
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException("Invalid json body: " + personJSON);
        }
    }

    public void setPersonAndIdToExchange(Exchange exchange) throws JsonProcessingException {
        String person = exchange.getIn().getBody(String.class);
        setPersonHeader(exchange, person);
        setUserIdHeader(exchange, objectMapper.readValue(person, Person.class).getId());
    }

    public void getRandomIdAndSetItToExchange(Exchange exchange) {
        setUserIdHeader(exchange, random.nextLong(countAllPeople()) + 1);
    }

    private void setPersonHeader(Exchange exchange, String person) {
        exchange.getMessage().setHeader("Person", person);
    }

    private void setUserIdHeader(Exchange exchange, long userId) {
        exchange.getMessage().setHeader("userId", userId);
    }

}
