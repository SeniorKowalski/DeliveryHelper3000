package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Класс-модель для заказов

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Product> orderedProducts;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    @Column(name = "delivery_date_time_start")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deliveryTimeWindowStart;

    @Column(name = "delivery_date_time_end")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deliveryTimeWindowEnd;

    @Column(name = "is_submitted")
    private Boolean submitted = false;

    @Column(name = "is_active")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne
    @JoinColumn(name = "route")
    private Route route;

    public void addProductToOrder(Product product) {
        if (orderedProducts != null) {
            product.setOrder(this);
            orderedProducts.add(product);
        } else {
            orderedProducts = new ArrayList<>();
            product.setOrder(this);
            orderedProducts.add(product);
        }
    }

    public void deleteProductFromOrder(Product product) {
        orderedProducts.remove(product);
        product.setOrder(null);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", car=" + car +
                ", orderDateTime=" + orderDateTime +
                ", deliveryTimeWindowStart=" + deliveryTimeWindowStart +
                ", deliveryTimeWindowEnd=" + deliveryTimeWindowEnd +
                ", submitted=" + submitted +
                ", active=" + active +
                '}';
    }
}