package vn.edu.iuh.fit.cartservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.cartservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
