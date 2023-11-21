package ru.kowalski.DeliveryHelper3000.util;

import org.springframework.stereotype.Component;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.model.Product;

import java.util.List;

// Класс-утилита для рассчёта объёма товаров

@Component
public class VolumeCalculator {

    // Считаем объём, занимаемый всеми продуктами в списке заказов
    public double calculateTotalProductVolume(List<Order> orders) {
        if (orders == null) {
            return 0;
        }

        double totalProductVolume = 0;
        for (Order order : orders) {
            totalProductVolume += calculateOneOrderVolume(order);
        }
        return totalProductVolume;
    }

    // объём, занимаемый продуктами из одного заказа, измеряемый в лотках (значение 1 = одному лотку)
    public double calculateOneOrderVolume(Order order) {
        double orderProductVolume = 0;
        for (Product product : order.getOrderedProducts()) {
            orderProductVolume += product.getProductSize() / 100;
        }
        return orderProductVolume;
    }
}
