package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.repository.OrderRepository;
import ru.kowalski.DeliveryHelper3000.util.OrderNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PartnerService partnerService;

    public void createNewOrder(Order order){
        orderRepository.save(order);
    }

    public void updateOrderById(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
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
