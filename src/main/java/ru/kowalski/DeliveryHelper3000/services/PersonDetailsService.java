package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Person;
import ru.kowalski.DeliveryHelper3000.repository.PersonRepository;
import ru.kowalski.DeliveryHelper3000.security.PersonDetails;

import java.util.Locale;
import java.util.Optional;

// Сервис для поиска пользователя, нужный для аутентификации

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByEmail(s.toLowerCase(Locale.ROOT));

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }
}
