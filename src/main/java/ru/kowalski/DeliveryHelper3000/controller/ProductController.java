package ru.kowalski.DeliveryHelper3000.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.services.ProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/new")
    public String createProductPage(@ModelAttribute("product") Product product){
        return "/product/new";
    }

    @PostMapping
    public String addProduct(@ModelAttribute("product") Product product){
        productService.createNewProduct(product);
        return "redirect:/admin";
    }

    @GetMapping("/show/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model){
        model.addAttribute("product",productService.getProductById(id));
        return "/partner/show";
    }
}
