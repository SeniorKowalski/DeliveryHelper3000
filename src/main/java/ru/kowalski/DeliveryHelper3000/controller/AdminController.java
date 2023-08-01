package ru.kowalski.DeliveryHelper3000.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kowalski.DeliveryHelper3000.services.AdminService;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.PersonService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PersonService personService;
    private final PartnerService partnerService;
    private final AdminService adminService;

    @GetMapping
    public String adminPage(Model model){
        model.addAttribute("persons", adminService.getAllPersons());
        model.addAttribute("products", adminService.getAllProducts());
        model.addAttribute("cars", adminService.getAllCars());
        return "/admin/index";
    }
}
