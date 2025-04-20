package vn.edu.iuh.fit.cartservice.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.iuh.fit.cartservice.model.*;
import vn.edu.iuh.fit.cartservice.repositories.CartItemRepository;
import vn.edu.iuh.fit.cartservice.repositories.CartRepository;
import vn.edu.iuh.fit.cartservice.repositories.ProductRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @PostConstruct
    @Transactional
    public void seedData() {
        // Thêm sản phẩm nếu chưa có
        if (productRepository.count() == 0) {
            Product apple = productRepository.save(new Product("Táo", 2.5));
            Product orange = productRepository.save(new Product("Cam", 3.0));
            Product banana = productRepository.save(new Product("Chuối", 1.5));

            System.out.println("Dữ liệu mẫu cho Product đã được thêm!");
        }

        if (cartRepository.count() == 0) {
            List<Product> products = productRepository.findAll();
            if (products.size() < 3) {
                System.out.println("Không đủ sản phẩm để tạo Cart!");
                return;
            }

            // Tạo Cart và lưu trước
            Cart cart = new Cart("Tran Anh Bao");
            cart = cartRepository.save(cart); // Lưu vào database trước khi thêm CartItem
            cartRepository.save(cart); // Cập nhật lại Cart với danh sách CartItem

            System.out.println("Dữ liệu mẫu cho Cart đã được thêm!");
        }
    }
}
