package vn.iuh.fit.se.productservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iuh.fit.se.productservice.models.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm kiếm sản phẩm theo tên
    List<Product> findByNameContaining(String name);

    // Lọc sản phẩm theo giá
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
}
