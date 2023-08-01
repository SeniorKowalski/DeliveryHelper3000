package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Car;
import ru.kowalski.DeliveryHelper3000.repository.CarRepository;
import ru.kowalski.DeliveryHelper3000.util.CarNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public void createNewCar(Car car){
        carRepository.save(car);
    }

    public void updateCarById(Long carId){
        Car updatedCar = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        carRepository.save(updatedCar);
    }

    public void deleteCarById(Long carId){
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        carRepository.deleteById(car.getCarId());
    }

    public Car findCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
    }

    public List<Car> findAllCars(){
        return carRepository.findAll();
    }
}
