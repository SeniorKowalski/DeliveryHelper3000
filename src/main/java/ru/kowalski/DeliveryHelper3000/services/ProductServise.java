package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.repository.ProductRepository;
import ru.kowalski.DeliveryHelper3000.util.ProductNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServise {

    private final ProductRepository productRepository;
    private final OrderService orderService;

    public Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public void createNewProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.deleteById(product.getProductId());
    }

    public void updateProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.save(product);
    }

    public List<Product> findProductsByOrderId(Long orderId){
        return orderService.findOrderById(orderId).getOrderedProducts();
    }

}
