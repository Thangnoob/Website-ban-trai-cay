package vn.edu.iuh.fit.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.fit.cartservice.model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Product product;
    private int quantity;
}

