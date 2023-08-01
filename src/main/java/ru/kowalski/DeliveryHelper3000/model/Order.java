package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

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
    private LocalDateTime deliveryTimeWindowStart;

    @Column(name = "delivery_date_time_end")
    private LocalDateTime deliveryTimeWindowEnd;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    public void addProductToOrder(Product product){
        product.setOrder(this);
        orderedProducts.add(product);
    }

    public void deleteProductFromOrder(Product product){
        orderedProducts.remove(product);
        product.setOrder(null);
    }

}