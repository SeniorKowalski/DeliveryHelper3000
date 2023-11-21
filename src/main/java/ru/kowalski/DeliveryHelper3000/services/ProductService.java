package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.exceptions.ProductNotFoundException;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.model.SelectedProductDTO;
import ru.kowalski.DeliveryHelper3000.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

// Сервис для операций с БД товаров в заказах

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public void createNewProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }

    public void updateProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.save(product);
    }

    public List<Product> getProductsByIds(List<Long> productIds) {
        List<Product> products = new ArrayList<>();
        for (long productId : productIds) {
            products.add(productRepository.findById(productId).orElseThrow(ProductNotFoundException::new));
        }
        return products;
    }

    public List<SelectedProductDTO> getProductsInDTO() {
        List<Product> products = getAllProducts();
        List<SelectedProductDTO> productsDTO = new ArrayList<>();
        for (Product product : products) {
            productsDTO.add(new SelectedProductDTO(product.getProductId(), product.getProductName()));
        }
        return productsDTO;
    }

    public SelectedProductDTO getTempProductDTO(long id) {
        Product product = getProductById(id);
        return new SelectedProductDTO(product.getProductId(), product.getProductName());
    }
}
