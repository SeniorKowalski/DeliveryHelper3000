package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kowalski.DeliveryHelper3000.exceptions.PersonNotFoundException;
import ru.kowalski.DeliveryHelper3000.model.Person;
import ru.kowalski.DeliveryHelper3000.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

// Сервис для операций с пользователями приложения

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(Long id) {
        Optional<Person> foundPerson = personRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }
}
