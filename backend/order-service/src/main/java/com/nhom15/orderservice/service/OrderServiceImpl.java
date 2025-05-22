package com.nhom15.orderservice.service;

import com.nhom15.orderservice.entity.Order;
import com.nhom15.orderservice.exception.CustomException;
import com.nhom15.orderservice.external.client.PaymentService;
import com.nhom15.orderservice.external.client.ProductService;
import com.nhom15.orderservice.external.request.PaymentRequest;
import com.nhom15.orderservice.external.response.PaymentResponse;
import com.nhom15.orderservice.model.OrderRequest;
import com.nhom15.orderservice.model.OrderResponse;
import com.nhom15.orderservice.external.response.ProductResponse;
import com.nhom15.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order entity --> save the data with Status Order Created
        //Product service - Block Products (reduce the quantity)
        //Payment service -> payment - > Success -> Complete or Cancelled


        log.info("Place order request: " + orderRequest);
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

        log.info("Calling payment Service to complete the payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null ;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment completed. Changing the order status to PLACE ");
            orderStatus="PLACED";
        } catch (Exception e) {
            log.error("Error occurred while place order. Changing order status to PAYMENT_FAILED");
            orderStatus="PAYMENT_FAILED";
        }

        order.setStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Placed: " + order);
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details: " + orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new CustomException("Order not found for order id: "+orderId, "NOT_FOUND",404));

        log.info("Invoking product service to get fetch the product for id: " + order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject(
                "http://product-service/product/" + order.getProductId(), ProductResponse.class
        );

        log.info("Getting payment information from the payment service");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://payment-service/payment/order/" + order.getId(), PaymentResponse.class
        );

        OrderResponse.PaymentDetails paymentDetails =
                OrderResponse.PaymentDetails
                        .builder()
                        .paymentDate(paymentResponse.getPaymentDate())
                        .paymentStatus(paymentResponse.getPaymentStatus())
                        .paymentId(paymentResponse.getPaymentId())
                        .paymentMode(paymentResponse.getPaymentMode())
                        .amount(paymentResponse.getAmount()).orderId(paymentResponse.getOrderId())
                        .build();

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails
                .builder()
                .name(productResponse.getName())
                .unit(productResponse.getUnit())
                .id(productResponse.getId())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .description(productResponse.getDescription())
                .image_url(productResponse.getImage_url()).build();


        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        return orderResponse;
    }
}
