package com.nhom15.productservice.model;

import com.nhom15.productservice.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private String image_url;
    private Long quantity;
    private Unit unit;
}
