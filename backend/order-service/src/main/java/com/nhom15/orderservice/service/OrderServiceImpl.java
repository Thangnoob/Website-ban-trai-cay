package com.nhom15.orderservice.service;

import com.nhom15.orderservice.entity.Order;
import com.nhom15.orderservice.external.client.ProductService;
import com.nhom15.orderservice.model.OrderRequest;
import com.nhom15.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order entity --> save the data with Status Order Created
        //Product service - Block Products (reduce the quantity)
        //Payment service -> payment - > Success -> Complete or Cancelled

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .status("CREATED")
                .userId(orderRequest.getUserId())
                .paymentMode(orderRequest.getPaymentMode())
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);
        log.info("Order Placed: " + order);
        return order.getId();
    }
}
