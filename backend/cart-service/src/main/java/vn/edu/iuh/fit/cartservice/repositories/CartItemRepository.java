package vn.edu.iuh.fit.cartservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.cartservice.model.CartItem;
import vn.edu.iuh.fit.cartservice.model.CartItemId;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
