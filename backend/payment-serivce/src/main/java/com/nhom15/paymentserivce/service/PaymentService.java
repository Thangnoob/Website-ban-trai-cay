package com.nhom15.paymentserivce.service;

import com.nhom15.paymentserivce.model.PaymentRequest;
import com.nhom15.paymentserivce.model.PaymentResponse;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(Long orderId);
}
