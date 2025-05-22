package com.nhom15.orderservice.external.response;


import com.nhom15.orderservice.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String image_url;
    private Long quantity;
    private Unit unit;

}
