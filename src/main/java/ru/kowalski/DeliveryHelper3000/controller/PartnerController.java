package ru.kowalski.DeliveryHelper3000.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.PersonService;
import ru.kowalski.DeliveryHelper3000.services.ProductService;

import java.util.List;

@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    private final PersonService personService;
    private final ProductService productService;

    @GetMapping()
    public String partnerPage(Model model) {
        model.addAttribute("partners", partnerService.findAll());
        return "/partner/index";
    }

    @GetMapping("/new")
    public String createPartnerPage(@ModelAttribute("partner") Partner partner) {
        return "/partner/new";
    }

    @PostMapping("/new")
    public String createNewPartner(@ModelAttribute("partner") Partner partner) {
        partnerService.save(partner);
        return "redirect:/partner";
    }

    @GetMapping("/show/{id}")
    public String showPartner(@PathVariable("id") Long id, Model model) {
        model.addAttribute("partner", partnerService.findPartnerById(id));
        return "/partner/show";
    }

    @GetMapping("/edit/{id}")
    public String editPartner(@PathVariable("id") Long id, Model model) {
        model.addAttribute("partner", partnerService.findPartnerById(id));
        return "/partner/edit";
    }

    @GetMapping("/getProducts")
    @ResponseBody
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

}
