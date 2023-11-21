package ru.kowalski.DeliveryHelper3000.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.Car;
import ru.kowalski.DeliveryHelper3000.services.CarService;

// Контроллер для операций с автомобилями

@Controller
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    @GetMapping("/new")
    public String createProductPage(@ModelAttribute("car") Car car) {
        return "/car/new";
    }

    @PostMapping
    public String addCar(@ModelAttribute("car") Car car) {
        carService.createNewCar(car);
        return "redirect:/admin";
    }

    @GetMapping("/show/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("car", carService.findCarById(id));
        return "/car/show";
    }
}
