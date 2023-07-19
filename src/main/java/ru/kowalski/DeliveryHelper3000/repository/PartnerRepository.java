package ru.kowalski.DeliveryHelper3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kowalski.DeliveryHelper3000.model.Partner;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner,Long> {
    List<Partner> findAllByPersonId(Long id);
}
