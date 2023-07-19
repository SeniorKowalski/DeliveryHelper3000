package ru.kowalski.DeliveryHelper3000.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Car {

    private String carId;
    private int carCapacity;
    private int[] baskets;

    public Car(String carId, int carCapacity, int[] baskets) {
        this.carId = carId;
        this.carCapacity = carCapacity;
        this.baskets = new int[carCapacity];
    }
}
