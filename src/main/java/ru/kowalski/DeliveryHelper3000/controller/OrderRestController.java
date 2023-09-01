package ru.kowalski.DeliveryHelper3000.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.*;
import ru.kowalski.DeliveryHelper3000.services.OrderService;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.ProductService;

import java.util.List;
import java.util.Map;


//@RestController
//@RequestMapping("/api/orders")
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final ProductService productService;
//    private final OrderService orderService;
//    private final PartnerService partnerService;
//
//    // Метод для получения всех продуктов в формате JSON
//    @GetMapping("/products")
//    public List<SelectedProductDTO> getAllProducts() {
//        return productService.getProductInDTO();
//    }
//
//    // Метод для создания заказа с выбранными продуктами
//    @PostMapping("/{partnerId}")
//    public ResponseEntity<String> createOrder(@PathVariable Long partnerId, @RequestBody Map<Long, Integer> selectedProducts) {
//        List<Partner> partners = partnerService.findAllPartnersByPersonId();
//        Partner currentPartner = partners.stream()
//                .filter(partner -> partner.getId().equals(partnerId))
//                .findFirst()
//                .orElse(null);
//
//        if (currentPartner == null) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
//        }
//
//        Order order = new Order();
//        order.setPartner(currentPartner);
//
//        for (Map.Entry<Long, Integer> entry : selectedProducts.entrySet()) {
//            Long productId = entry.getKey();
//            Integer quantity = entry.getValue();
//
//            Product product = productService.getProductById(productId);
//            if (product == null) {
//                return ResponseEntity.badRequest().body("Invalid product ID: " + productId);
//            }
//
//            product.setProductQuantity(quantity);
//            order.addProductToOrder(product);
//        }
//
//        orderService.createOrder(order);
//
//        return ResponseEntity.ok("Order created successfully");
//    }
//}

//@Controller
//@RequestMapping("/order")
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final ProductService productService;
//    private final OrderService orderService;
//    private final PartnerService partnerService;
//
//    @GetMapping("/createOrder/{partnerId}")
//    public String createOrderPage(@PathVariable("partnerId") Long partnerId, Model model) {
//        model.addAttribute("partner", partnerService.findPartnerById(partnerId));
//        model.addAttribute("allProducts", productService.getAllProducts());
//        return "/order/CreateOrder";
//    }
//
//    @GetMapping("/products")
//    public List<SelectedProductDTO> getAllProducts() {
//        return productService.getProductInDTO();
//    }
//
//    @PostMapping("/createOrder")
//    public ResponseEntity<String> createOrder(@RequestParam Long partnerId, @RequestParam String selectedProductsJson) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Product> selectedProducts;
//        try {
//            selectedProducts = objectMapper.readValue(selectedProductsJson, new TypeReference<>() {
//            });
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().body("Error parsing selected products.");
//        }
//
//        List<Partner> partners = partnerService.findAllPartnersByPersonId();
//        Partner currentPartner = partners.stream()
//                .filter(partner -> partner.getId().equals(partnerId))
//                .findFirst()
//                .orElse(null);
//
//        if (currentPartner == null) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
//        }
//
//        Order order = new Order();
//        order.setPartner(currentPartner);
//
//        for (Product selectedProduct : selectedProducts) {
//            selectedProduct.setOrder(order);
//            order.getOrderedProducts().add(selectedProduct);
//        }
//
//        orderService.createOrder(order);
//
//        return ResponseEntity.ok("Order created successfully");
//    }
//}

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final ProductService productService;
    private final OrderService orderService;
    private final PartnerService partnerService;

    @PostMapping("/{partnerId}")
    public ResponseEntity<String> createOrder(@PathVariable Long partnerId, @RequestBody List<SelectedProductDTO> selectedProducts) {
        List<Partner> partners = partnerService.findAllPartnersByPersonId();
        Partner currentPartner = partners.stream()
                .filter(partner -> partner.getId().equals(partnerId))
                .findFirst()
                .orElse(null);
        System.out.println(partners);

        if (currentPartner == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        Order order = new Order();
        order.setPartner(currentPartner);

        for (SelectedProductDTO selectedProduct : selectedProducts) {
            Product product = productService.getProductById(selectedProduct.getProductId());
            if (product == null) {
                return ResponseEntity.badRequest().body("Invalid product ID: " + selectedProduct.getProductId());
            }

            product.setProductQuantity(selectedProduct.getQuantity());
            order.addProductToOrder(product);
        }

        orderService.createOrder(order);

        return ResponseEntity.ok("Order created successfully");
    }
}