package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.exceptions.OrderNotFoundException;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

// Сервис для взаимодейтвия с БД заказов

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PartnerService partnerService;
    private final ProductService productService;

    public void createNewOrder(Long partnerId, List<Long> productIds, LocalDateTime deliveryTimeWindowStart, LocalDateTime deliveryTimeWindowEnd) {

        Order order = new Order();

        List<Product> products = productService.getProductsByIds(productIds);
        order.setOrderedProducts(products);

        order.setDeliveryTimeWindowStart(deliveryTimeWindowStart);
        order.setDeliveryTimeWindowEnd(deliveryTimeWindowEnd);

        Partner partner = partnerService.findPartnerById(partnerId);
        order.setPartner(partner);

        order.setOrderDateTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public void updateOrder(Order updatedOrder) {
        Order order = orderRepository.findById(updatedOrder.getId()).orElseThrow(OrderNotFoundException::new);
        orderRepository.save(order);
    }

    public void deleteOrderById(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        orderRepository.deleteById(order.getId());
    }

    public Order findOrderById(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public List<Order> findAllOrdersByPartnerId(long partnerId) {
        return partnerService.findPartnerById(partnerId).getOrders();
    }

    public Order findLastPartnersOrder(long partnerId) {
        List<Order> orders = findAllOrdersByPartnerId(partnerId);
        if (!orders.isEmpty()) {
            return orders.get(orders.size() - 1);
        }
        return null;
    }

    public Order createNewOrder(Partner currentPartner, Product product) {
        Order order = new Order();
        order.setPartner(currentPartner);
        order.addProductToOrder(product);
        order.setOrderDateTime(LocalDateTime.now());
        return order;
    }

    public List<Order> getAllActiveOrders() {
        return orderRepository.findOrdersByActive(true);
    }

    public List<Order> getOrdersByDeliveryDate() {
        LocalDate todayDate = LocalDateTime.now().toLocalDate();
        return orderRepository.findAll().stream().filter(order -> Objects.equals(order.getDeliveryTimeWindowStart().toLocalDate(), todayDate)).collect(Collectors.toList());
    }

    public Optional<Order> findTodayFirstOrder(List<Order> activeOrders) {
        return activeOrders.stream()
                .min(Comparator.comparing(Order::getDeliveryTimeWindowStart));
    }
}
