package com.example.camelproject.sevices;

import com.example.camelproject.exceptions.PersonNotFoundException;
import com.example.camelproject.models.Person;
import com.example.camelproject.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public long countAllPeople() {
        return personRepository.count();
    }

    public Person getPersonById(int id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("Person with id " + id + " not found"));
    }

    @Transactional
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }


}
