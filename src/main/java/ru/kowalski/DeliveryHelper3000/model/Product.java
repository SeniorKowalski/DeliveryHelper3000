package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Entity
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

    @NotEmpty(message = "Нужно указать цену продукта")
    @Size(min = 1, max = 64)
    @Column(name = "product_price")
    private int productPrice;

    @NotEmpty()
    @Size(min = 1, max = 3)
    @Column(name = "product_size")
    private double productSize;

    @Size(min = 1, max = 64)
    @Column(name = "product_quantity")
    private int productQuantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
