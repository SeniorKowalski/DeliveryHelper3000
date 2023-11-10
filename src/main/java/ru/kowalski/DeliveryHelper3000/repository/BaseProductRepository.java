package ru.kowalski.DeliveryHelper3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kowalski.DeliveryHelper3000.model.BaseProduct;
import ru.kowalski.DeliveryHelper3000.model.Product;

@Repository
public interface BaseProductRepository extends JpaRepository<BaseProduct,Long> {
}
