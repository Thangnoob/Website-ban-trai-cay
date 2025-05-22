package com.nhom15.orderservice.external.client;

import com.nhom15.orderservice.exception.CustomException;
import com.nhom15.orderservice.external.response.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "product-service/product")
public interface ProductService {
    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") Long productId,
            @RequestParam long quantity
    );

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProduct(@PathVariable long id);

    default void fallback(Exception e) {
        throw new CustomException("Product Service is not available", "UNAVAILABLE", 500);
    }
}
