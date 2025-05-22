package vn.iuh.fit.se.productservice.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "product_service")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stock_quantity")
    private Long stockQuantity;

    @Column(name = "image_path", length = 500)
    private String imagePath;  // Lưu đường dẫn hình ảnh

    public Product(Long id, String name, double price, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}