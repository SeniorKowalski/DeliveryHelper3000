package ru.kowalski.DeliveryHelper3000.util;

import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.model.Product;

import java.util.List;

public class VolumeCalculator {

    // Считаем объём, занимаемый всеми продуктами в 1 заказе
    public double calculateTotalProductVolume(List<Order> orders) {
        if (orders == null) {
            return 0;
        }

        double totalProductVolume = 0;
        for (Order order : orders) {
            for (Product product : order.getOrderedProducts()) {
                totalProductVolume += (product.getProductSize() / 100) * product.getProductQuantity();
            }
        }
        return totalProductVolume;
    }
}
