package ru.kowalski.DeliveryHelper3000.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.ProductService;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderPageController {

    private final PartnerService partnerService;
    private final ProductService productService;

    @GetMapping("/createOrder/{id}")
    public String createOrderPage(@PathVariable("id") Long partnerId, Model model) {
        model.addAttribute("partner", partnerService.findPartnerById(partnerId));
        model.addAttribute("allProducts", productService.getProductsInDTO());
        return "/order/createOrder";
    }
}