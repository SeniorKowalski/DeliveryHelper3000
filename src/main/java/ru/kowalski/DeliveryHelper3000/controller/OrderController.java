package ru.kowalski.DeliveryHelper3000.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kowalski.DeliveryHelper3000.services.OrderService;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.ProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final PartnerService partnerService;
    private final ProductService productService;

    @GetMapping("/new/{id}")
    public String orderPage(@PathVariable("id") Long id, Model model){
        model.addAttribute("partner",partnerService.findPartnerById(id));
        return "/order/new";
    }

    @PostMapping("/new/{id}")
    public String createNewOrder(@PathVariable("id") Long id, Model model){
        model.addAttribute("partner",partnerService.findPartnerById(id));
        model.addAttribute("products",productService.getAllProducts());
//        orderService.createNewOrder(id,);
        return "redirect:/partner/show/{id}";
    }

}
