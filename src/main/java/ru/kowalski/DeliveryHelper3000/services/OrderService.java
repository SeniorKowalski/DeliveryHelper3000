package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Car;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.repository.OrderRepository;
import ru.kowalski.DeliveryHelper3000.util.OrderNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PartnerService partnerService;
    private final ProductService productService;
    private final CarService carService;

    public void createNewOrder(Partner partner, List<Long> productIds, Long carId, LocalDateTime deliveryDateTime){

        Order order = new Order();

        List<Product> products = productService.getProductsByIds(productIds);
        order.setOrderedProducts(products);

        order.setDeliveryDateTime(deliveryDateTime);

        order.setPartner(partner);

        Car car = carService.getCarById(carId);
        order.setCar(car);

        order.setOrderDateTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void updateOrder(Order updatedOrder){
        Order order = orderRepository.findById(updatedOrder.getOrderId()).orElseThrow(OrderNotFoundException::new);
        orderRepository.save(order);
    }

    public void deleteOrderById(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        orderRepository.deleteById(order.getOrderId());
    }

    public Order findOrderById(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public List<Order> findAllOrdersByPartnerId(Long partnerId){
        return partnerService.findPartnerById(partnerId).getOrders();
    }



}
