package com.nhom15.orderservice.service;

import com.nhom15.orderservice.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
