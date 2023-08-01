package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "car")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @NotEmpty(message = "Номер машины не может быть пустым")
    @Size(min = 1, max = 8, message = "Номер машины должен содержать от 1 до 8 символов")
    @Column(name = "car_number")
    private String carNumber;

    @Min(value = 1, message = "Вместимость машины (в лотках) должна быть больше нуля")
    @Column(name = "car_capacity")
    private int carCapacity;

    @OneToMany(mappedBy = "car", cascade = CascadeType.DETACH, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders;

    public void addOrder(Order order){
        order.setCar(this);
        orders.add(order);
    }
    public void deleteOrder(Order order){
        orders.remove(order);
        order.setCar(null);
    }
}
