package vn.edu.iuh.fit.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private int id;
    private String name;
    private List<CartItemDTO> items = new ArrayList<>();

    public CartDTO(int id, String name) {
        this.id = id;
        this.name = name;
        this.items = new ArrayList<>();
    }

}

