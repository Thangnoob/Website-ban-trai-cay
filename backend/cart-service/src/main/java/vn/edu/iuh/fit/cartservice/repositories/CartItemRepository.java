package vn.edu.iuh.fit.cartservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.cartservice.model.Cart;
import vn.edu.iuh.fit.cartservice.model.CartItem;
import vn.edu.iuh.fit.cartservice.model.CartItemId;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartIdAndProductId(int cartId, int productId);
}
