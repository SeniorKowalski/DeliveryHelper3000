package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Person;
import ru.kowalski.DeliveryHelper3000.repository.PartnerRepository;
import ru.kowalski.DeliveryHelper3000.repository.PersonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminService {

    private final PersonRepository personRepository;
    private final PartnerRepository partnerRepository;

//    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_SOME_OTHER')")
//    public void doAdminStuff() {
//        System.out.println("Only admin here");
//    }

    public List<Person> getAllPersons(){
        return personRepository.findAll();
    }
}
