package ru.kowalski.DeliveryHelper3000.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kowalski.DeliveryHelper3000.model.*;
import ru.kowalski.DeliveryHelper3000.services.BaseProductService;
import ru.kowalski.DeliveryHelper3000.services.OrderService;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.ProductService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

// Rest-контроллер для добавления товара к заказу

@RestController
@RequiredArgsConstructor
public class OrderRestController {

    private final ProductService productService;
    private final BaseProductService baseProductService;
    private final OrderService orderService;
    private final PartnerService partnerService;

    @Transactional
    @PostMapping("/api/orders/{partnerId}/{productId}")
    public ResponseEntity<String> addProductToOrder(@PathVariable Long partnerId, @PathVariable Long productId, SelectedProductDTO selectedProduct, HttpServletResponse response) throws IOException {
        // Проверка что id партнёра, указанный в юрле принадлежит текущему пользователю.
        List<Partner> partners = partnerService.findAllPartnersByPersonId();
        Partner currentPartner = partners.stream()
                .filter(partner -> partner.getId().equals(partnerId))
                .findFirst()
                .orElse(null);

        if (currentPartner == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access to this page denied.");
        }

        // Если у currentPartner есть getLastOrder и он не isSubmitted, то добавляем продукт в него, иначе создаём новый Order
        BaseProduct tempProduct = baseProductService.getProductById(productId);
        Product product = new Product();
        product.setProductName(tempProduct.getProductName());
        product.setProductPrice(tempProduct.getProductPrice());
        product.setProductSum(tempProduct.getProductPrice() * selectedProduct.getQuantity());
        product.setProductSize(tempProduct.getProductSize() * selectedProduct.getQuantity());
        product.setProductQuantity(selectedProduct.getQuantity());
        productService.createNewProduct(product);

        if (orderService.findLastPartnersOrder(currentPartner.getId()) != null) {
            if (!orderService.findLastPartnersOrder(currentPartner.getId()).getSubmitted()) {
                Order tempOrder = orderService.findLastPartnersOrder(currentPartner.getId());
                tempOrder.setOrderDateTime(LocalDateTime.now());
                tempOrder.addProductToOrder(product);
            } else {
                Order order = orderService.createNewOrder(currentPartner, product);
                orderService.createOrder(order);
            }
        } else {
            Order order = orderService.createNewOrder(currentPartner, product);
            orderService.createOrder(order);
        }

        response.sendRedirect("/order/createOrder/" + currentPartner.getId());
        return ResponseEntity.ok("Product added to order successfully");
    }


}