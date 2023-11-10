package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.BaseProduct;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.model.SelectedProductDTO;
import ru.kowalski.DeliveryHelper3000.repository.BaseProductRepository;
import ru.kowalski.DeliveryHelper3000.repository.ProductRepository;
import ru.kowalski.DeliveryHelper3000.util.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseProductService {

    private final BaseProductRepository productRepository;
//    private final OrderService orderService;

    public List<BaseProduct> getAllProducts(){
        return productRepository.findAll();
    }

    public BaseProduct getProductById(long id){
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public void createNewProduct(BaseProduct product){
        productRepository.save(product);
    }

    public void deleteProductById(long id){
        productRepository.deleteById(id);
    }

    public void updateProductById(long id){
        BaseProduct product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.save(product);
    }

//    public List<Product> findProductsByOrderId(Long orderId){
//        return orderService.findOrderById(orderId).getOrderedProducts();
//    }
    //TODO убрать цикличную зависимость

    public List<BaseProduct> getProductsByIds(List<Long> productIds) {
        List<BaseProduct> products = new ArrayList<>();
        for (long productId : productIds) {
            products.add(productRepository.findById(productId).orElseThrow(ProductNotFoundException::new));
        }
        return products;
    }

    public List<SelectedProductDTO> getProductsInDTO(){
        List<BaseProduct> products = getAllProducts();
        List<SelectedProductDTO> productsDTO = new ArrayList<>();
        for (BaseProduct product: products){
            productsDTO.add(new SelectedProductDTO(product.getProductId(), product.getProductName()));
        }
        return productsDTO;
    }

    public SelectedProductDTO getTempProductDTO(long id){
        BaseProduct product = getProductById(id);
        return new SelectedProductDTO(product.getProductId(), product.getProductName());
    }
}
