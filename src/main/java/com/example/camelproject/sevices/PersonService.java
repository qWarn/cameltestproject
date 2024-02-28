package com.example.camelproject.sevices;

import com.example.camelproject.exceptions.EmailIsAlreadyUsedException;
import com.example.camelproject.exceptions.PersonNotFoundException;
import com.example.camelproject.models.Person;
import com.example.camelproject.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

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

    public void setPersonToExchange(Exchange exchange) {
        exchange.getMessage().setBody(exchange.getIn().getBody(Person.class));
    }

    public long getRandomId() {
        return random.nextLong(countAllPeople()) + 1;
    }

}
