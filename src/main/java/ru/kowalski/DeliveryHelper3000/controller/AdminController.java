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
import ru.kowalski.DeliveryHelper3000.util.VolumeCalculator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Класс-контроллер для администрирования приложения (товары, автомобили,маршруты)

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final DistanceCalculator distanceCalculator;
    private final VolumeCalculator volumeCalculator;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("persons", adminService.getAllPersons());
        model.addAttribute("products", adminService.getAllProducts());
        model.addAttribute("allCars", adminService.getAllCars());
        model.addAttribute("car", new Car());

        // проверяем, есть ли активные заказы, если да, то выводим их сразу в виде оптимального маршрута.
        // тут есть проблема в том, что если точка по времени не попадает в маршрут, то и не отображается.
        // возможно целесообразнее будет выводить просто все активные маршруты, как это было раньше.
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

        List<Order> activeOrders = adminService.getAllActiveOrders();
        List<Partner> partnerList = distanceCalculator.createRouteWithTime(activeOrders, timeOfStart);
        List<Order> routeOrders = new ArrayList<>();
        for (Order order :
                activeOrders) {
            for (Partner partner :
                    partnerList) {
                if (order.getPartner().equals(partner)) {
                    routeOrders.add(order);
                }
            }
        }
        //TODO сделать обработчик ошибок с возвращением статуса.

        // пока объём продуктов больше объёма авто - убираем последнюю точку маршрута.
        while (volumeCalculator.calculateTotalProductVolume(routeOrders) > selectedCar.getCarCapacity()) {
            if (routeOrders.size() > 3) {
                routeOrders.remove(routeOrders.size() - 2);
            } else {
                System.out.println("Выберите автомобиль с большим объёмом для доставки данных заказов.");
                break;
            }
        }
        // если в маршруте есть хотя бы одна точка кроме склада, то считаем его корректным и назначаем ему машину.
        if (!routeOrders.isEmpty()) {
            for (Order order :
                    routeOrders) {
                order.setCar(selectedCar);
                order.setActive(false);
            }
            route.setOrders(routeOrders);
            route.setTimeOfStartDelivery(timeOfStart);
            adminService.createNewRoute(route);
        }

        // сделать вывод загруженности авто.
        double capacity = selectedCar.getCarCapacity() - volumeCalculator.calculateTotalProductVolume(routeOrders);

        model.addAttribute("capacity", capacity);

        return "redirect:/admin";
    }

}
