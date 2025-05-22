package com.nhom15.orderservice.external.response;

import com.nhom15.orderservice.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private Long paymentId;
    private String paymentStatus;
    private long orderId;
    private long amount;
    private Instant paymentDate;
    private PaymentMode paymentMode;
}

