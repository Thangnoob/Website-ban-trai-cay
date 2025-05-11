package com.nhom15.productservice.service;


import com.nhom15.productservice.model.ProductRequest;
import com.nhom15.productservice.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long id);
}
