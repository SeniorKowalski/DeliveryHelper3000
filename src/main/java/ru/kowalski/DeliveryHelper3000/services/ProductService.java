package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.model.SelectedProductDTO;
import ru.kowalski.DeliveryHelper3000.repository.ProductRepository;
import ru.kowalski.DeliveryHelper3000.util.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
//    private final OrderService orderService;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public void createNewProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

    public void updateProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.save(product);
    }

//    public List<Product> findProductsByOrderId(Long orderId){
//        return orderService.findOrderById(orderId).getOrderedProducts();
//    }
    //TODO убрать цикличную зависимость

    public List<Product> getProductsByIds(List<Long> productIds) {
        List<Product> products = new ArrayList<>();
        for (Long productId : productIds) {
            products.add(productRepository.findById(productId).orElseThrow(ProductNotFoundException::new));
        }
        return products;
    }

    public List<SelectedProductDTO> getProductsInDTO(){
        List<Product> products = getAllProducts();
        List<SelectedProductDTO> productsDTO = new ArrayList<>();
        for (Product product: products){
            productsDTO.add(new SelectedProductDTO(product.getProductId(), product.getProductName()));
        }
        return productsDTO;
    }
}
