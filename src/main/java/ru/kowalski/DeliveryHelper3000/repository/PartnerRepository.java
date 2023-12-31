package ru.kowalski.DeliveryHelper3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kowalski.DeliveryHelper3000.model.Partner;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner,Long> {
    List<Partner> findAllByPersonId(Long id);
}
