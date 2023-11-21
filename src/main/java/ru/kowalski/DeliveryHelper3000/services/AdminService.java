package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.*;
import ru.kowalski.DeliveryHelper3000.repository.PersonRepository;
import ru.kowalski.DeliveryHelper3000.util.DistanceCalculator;

import java.util.List;
import java.util.Optional;

// Сервис для администрирования приложения

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminService {

    private final PersonRepository personRepository;
    private final BaseProductService baseProductService;
    private final CarService carService;
    private final OrderService orderService;
    private final RouteService routeService;
    private final DistanceCalculator distanceCalculator;

//    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_SOME_OTHER')")
//    public void doAdminStuff() {
//        System.out.println("Only admin here");
//    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public List<BaseProduct> getAllProducts() {
        return baseProductService.getAllProducts();
    }

    public List<Car> getAllCars() {
        return carService.findAllCars();
    }

    public List<Car> getAllFreeCars() {
        return carService.getAllFreeCars();
    }

    public List<Order> getAllActiveOrders() {
        return orderService.getAllActiveOrders();
    }

    public Order getFirstTodayOrder(List<Order> activeOrders) {
        Optional<Order> firstOrder = orderService.findTodayFirstOrder(activeOrders);
        return firstOrder.orElse(null);
    }

    public List<Order> getAllTodayOrders() {
        return orderService.getOrdersByDeliveryDate();
    }

    public List<Partner> getRouteWithAllActiveOrders() {
        return distanceCalculator.createRouteWithTime(getAllActiveOrders(),
                        getFirstTodayOrder(getAllActiveOrders()).getDeliveryTimeWindowStart())
                .stream()
                .filter(partner -> partner.getId() != null)
                .toList();
    }

    public void createNewRoute(Route route) {
        routeService.createNewRoute(route);
    }

}
