package com.nhom15.orderservice.service;

import com.nhom15.orderservice.model.OrderRequest;
import com.nhom15.orderservice.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
