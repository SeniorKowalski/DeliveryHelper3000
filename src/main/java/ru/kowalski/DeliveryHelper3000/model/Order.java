package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Partner partner;
    private Product product;
    private Car car;
    private long orderDateTime;
    private Date deliveryDateTime;

}