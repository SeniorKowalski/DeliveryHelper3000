package ru.kowalski.DeliveryHelper3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kowalski.DeliveryHelper3000.model.Car;

public interface CarRepository extends JpaRepository<Car,Long> {
}
