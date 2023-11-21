package ru.kowalski.DeliveryHelper3000.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kowalski.DeliveryHelper3000.model.Person;
import ru.kowalski.DeliveryHelper3000.services.PersonDetailsService;

import java.util.Locale;

// Класс-утилита для валидации данных пользователя при регистрации

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        try {
            personDetailsService.loadUserByUsername(person.getEmail().toLowerCase(Locale.ROOT));
        } catch (UsernameNotFoundException ignored) {
            return; // все ок, пользователь не найден
        }

        errors.rejectValue("email", "", "Такой email уже зарегистрирован");
    }
}
