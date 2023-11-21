package ru.kowalski.DeliveryHelper3000.model;

import lombok.Data;

// Класс DTO, используемый для домавления товаров в заказ

@Data
public class SelectedProductDTO {

    private Long productId;
    private String productName;
    private int quantity;

    public SelectedProductDTO(Long productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }
}