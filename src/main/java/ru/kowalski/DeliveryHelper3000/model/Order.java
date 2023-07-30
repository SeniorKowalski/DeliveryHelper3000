package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Product> orderedProducts;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "order_date_time")
    private Long orderDateTime;

    @Column(name = "delivery_date_time")
    private Long deliveryDateTime;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

}