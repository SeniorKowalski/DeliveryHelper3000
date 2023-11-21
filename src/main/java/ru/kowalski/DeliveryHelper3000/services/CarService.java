package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.exceptions.CarNotFoundException;
import ru.kowalski.DeliveryHelper3000.model.Car;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.repository.CarRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Сервис для операций с автомобилями доставки в БД

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public void createNewCar(Car car) {
        carRepository.save(car);
    }

    public void updateCarById(Long carId) {
        Car updatedCar = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        carRepository.save(updatedCar);
    }

    public void deleteCarById(Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        carRepository.deleteById(car.getCarId());
    }

    public Car findCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
    }

    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    public List<Order> findOrdersByCarId(long carId) {
        if (carRepository.findById(carId).isPresent()) {
            if (!carRepository.findById(carId).get().getOrders().isEmpty())
                return carRepository.findById(carId).get().getOrders();
        }
        return null;
    }

    public List<Car> getAllFreeCars() {
        return carRepository.findAll().stream()
                .filter(car -> {
                    Optional<Order> lastOrderOptional = car.getLastOrderOfThisCar();
                    return lastOrderOptional.map(order -> order.getDeliveryTimeWindowEnd().isBefore(LocalDateTime.now())).orElse(true);
                })
                .sorted(Comparator.comparing(Car::getCarId))
                .collect(Collectors.toList());
    }
}
