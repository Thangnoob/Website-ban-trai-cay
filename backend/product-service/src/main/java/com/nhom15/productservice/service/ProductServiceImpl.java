package com.nhom15.productservice.service;

import com.nhom15.productservice.model.ProductRequest;
import com.nhom15.productservice.model.ProductResponse;
import com.nhom15.productservice.exception.ProductServiceCustomException;
import com.nhom15.productservice.entity.Product;
import com.nhom15.productservice.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
       log.info("Adding product");

       Product product = Product.builder()
               .name(productRequest.getName())
               .price(productRequest.getPrice())
               .description(productRequest.getDescription())
               .image_url(productRequest.getImage_url())
               .quantity(productRequest.getQuantity())
               .unit(productRequest.getUnit())
               .build();

        productRepository.save(product);
        log.info("Product added");
        return product.getId();
    }

    @Override
    public ProductResponse getProductById(long id) {
        log.info("Getting product by id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductServiceCustomException("Product not found","PRODUCT_NOT_FOUND"));
        ProductResponse productResponse = new ProductResponse();
        copyProperties(product, productResponse);
        return productResponse;
    }
}
