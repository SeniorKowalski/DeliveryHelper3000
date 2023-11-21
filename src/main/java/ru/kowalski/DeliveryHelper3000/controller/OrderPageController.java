package ru.kowalski.DeliveryHelper3000.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.services.BaseProductService;
import ru.kowalski.DeliveryHelper3000.services.OrderService;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;

// Контроллер для операций с завказами

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderPageController {

    private final PartnerService partnerService;
    private final BaseProductService productService;
    private final OrderService orderService;

    @GetMapping("/createOrder/{partnerId}")
    public String createOrderPage(@PathVariable("partnerId") long partnerId, Model model, @ModelAttribute("order") Order order) {
        model.addAttribute("partner", partnerService.findPartnerById(partnerId));
        model.addAttribute("allProducts", productService.getProductsInDTO());
        if (orderService.findLastPartnersOrder(partnerId) != null && !orderService.findLastPartnersOrder(partnerId).getSubmitted()) {
            model.addAttribute("orderedProducts", orderService.findLastPartnersOrder(partnerId).getOrderedProducts());
        }
        return "/order/createOrder";
    }

    @GetMapping("/product/{partnerId}/{productId}")
    public String orderProduct(@PathVariable("partnerId") long partnerId, @PathVariable("productId") long productId, Model model) {
        model.addAttribute("partner", partnerService.findPartnerById(partnerId));
        model.addAttribute("selectedProduct", productService.getTempProductDTO(productId));
        return "/order/product";
    }

    @PostMapping("/createOrder/{partnerId}")
    public String submitOrder(@PathVariable("partnerId") long partnerId, Model model, @ModelAttribute("order") Order order) {
        model.addAttribute("partner", partnerService.findPartnerById(partnerId));
        Order tempOrder = orderService.findLastPartnersOrder(partnerId);
        tempOrder.setDeliveryTimeWindowStart(order.getDeliveryTimeWindowStart());
        tempOrder.setDeliveryTimeWindowEnd(order.getDeliveryTimeWindowStart().plusHours(2));
        tempOrder.setSubmitted(true);
        orderService.updateOrder(tempOrder);
        return "redirect:/partner";
    }
}