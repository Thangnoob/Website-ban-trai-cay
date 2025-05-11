package com.nhom15.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Long userId;
    private long productId;
    private long totalAmount;
    private long quantity;
    private PaymentMode paymentMode;
}
