package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

// Класс-модель товара, используемая для заказов

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotEmpty(message = "Нужно указать название продукта")
    @Size(min = 2, max = 128, message = "Название должно содержать от 2 до 128 символов")
    @Column(name = "product_name")
    private String productName;

    @NotNull(message = "Нужно указать цену продукта")
    @Min(value = 1, message = "Цена продуктов должна быть больше нуля")
    @Column(name = "product_price")
    private Double productPrice;

    @Min(value = 1, message = "Цена продуктов должна быть больше нуля")
    @Column(name = "product_sum")
    private Double productSum;

    @NotNull(message = "Нужно указать размер продукта")
    @Min(value = 1, message = "Размер продуктов в % должен быть больше нуля")
    @Max(value = 100, message = "Размер продуктов в % не может превышать 100")
    @Column(name = "product_size")
    private Double productSize;

    //    @Min(value = 1, message = "Количество продуктов должно быть больше нуля")
    @Column(name = "product_quantity")
    private int productQuantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
