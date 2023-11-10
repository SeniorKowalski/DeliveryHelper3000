package ru.kowalski.DeliveryHelper3000.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.Car;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.model.Route;
import ru.kowalski.DeliveryHelper3000.services.AdminService;
import ru.kowalski.DeliveryHelper3000.util.DistanceCalculator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final DistanceCalculator distanceCalculator;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("persons", adminService.getAllPersons());
        model.addAttribute("products", adminService.getAllProducts());
        model.addAttribute("allCars", adminService.getAllCars());
        model.addAttribute("car", new Car());

        // проверяем, есть ли активные заказы, если да, то выводим их сразу в виде оптимального маршрута.
        if (!adminService.getAllActiveOrders().isEmpty()) {
            model.addAttribute("partners", adminService.getRouteWithAllActiveOrders());
        }
        if (!adminService.getAllFreeCars().isEmpty()) {
            model.addAttribute("freeCars", adminService.getAllFreeCars());
        } else {
            model.addAttribute("freeCars", adminService.getAllCars());
        }

        return "/admin/index";
    }

    //TODO дописать метод отправки данных для получения маршрута
    @PostMapping("/route")
    public String calculateRoute(@ModelAttribute("route") Route route,
                                 @ModelAttribute("car") Car selectedCar,
                                 @RequestParam(value = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime, Model model) {
        String time = String.valueOf(startTime);
        LocalDateTime timeOfStart = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
//        LocalDateTime timeOfStart = LocalDateTime.parse(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
//TODO доделать submit active orders и чтобы корректно выбиралась машина
        List<Order> activeOrders = adminService.getAllActiveOrders();
        List<Partner> partnerList = distanceCalculator.createRouteWithTime(activeOrders, timeOfStart);
        List<Order> routeOrders = new ArrayList<>();
        for (Order order :
                activeOrders) {
            for (Partner partner :
                    partnerList) {
                if (order.getPartner().equals(partner)) {
                    routeOrders.add(order);
                    order.setCar(selectedCar);
                    order.setActive(false);
                }
            }
        }
        route.setOrders(routeOrders);
        route.setTimeOfStartDelivery(timeOfStart);
        adminService.createNewRoute(route);
        return "redirect:/admin";
    }

}
