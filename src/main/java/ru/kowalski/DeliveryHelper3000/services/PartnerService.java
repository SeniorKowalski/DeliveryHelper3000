package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.model.Person;
import ru.kowalski.DeliveryHelper3000.repository.PartnerRepository;
import ru.kowalski.DeliveryHelper3000.security.PersonDetails;
import ru.kowalski.DeliveryHelper3000.util.PartnerNotFoundException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartnerService {

    private final PartnerRepository partnerRepository;


    public List<Partner> findAll(){
        Person person = getCurrentPerson();
        return partnerRepository.findAllByPersonId(person.getId());
    }

    public Partner findOne(Long id){
        Person person = getCurrentPerson();
        return partnerRepository.findAllByPersonId(person.getId()).stream()
                .filter(partner -> Objects.equals(partner.getId(), id))
                .findAny().orElseThrow(PartnerNotFoundException::new);
    }

    @Transactional
    public void save(Partner partner){
        Person person = getCurrentPerson();
        person.addPartner(partner);
        partnerRepository.save(partner);
    }

    private Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson();
    }
}
