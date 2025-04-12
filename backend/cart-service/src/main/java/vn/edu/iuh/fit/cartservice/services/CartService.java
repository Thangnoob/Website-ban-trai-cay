package vn.edu.iuh.fit.cartservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.cartservice.dto.CartDTO;
import vn.edu.iuh.fit.cartservice.dto.CartItemDTO;
import vn.edu.iuh.fit.cartservice.model.Cart;
import vn.edu.iuh.fit.cartservice.model.CartItem;
import vn.edu.iuh.fit.cartservice.model.CartItemId;
import vn.edu.iuh.fit.cartservice.model.Product;
import vn.edu.iuh.fit.cartservice.repositories.CartItemRepository;
import vn.edu.iuh.fit.cartservice.repositories.CartRepository;
import vn.edu.iuh.fit.cartservice.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Cart getCart(int cartId) {
        return cartRepository.findById(cartId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setId(cartId);
            return cartRepository.save(newCart);
        });
    }

    @Transactional
    public void addToCart(int cartId, int productId, int quantity) {
        if (quantity < 0) {
            throw new RuntimeException("Số lượng không hợp lệ");
        }

        Cart cart = getCart(cartId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            cartItemRepository.save(existingItem.get());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    public void updateCartItem(int cartId, int productId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(new CartItemId(cartId, productId)).orElseThrow(() -> new RuntimeException("Không có sản phẩm này trong giỏ"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public CartDTO getCartDTO(int cartId) {
        Cart cart = cartRepository.findById(cartId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setId(cartId);
            return cartRepository.save(newCart);
        });

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(item.getProduct(), item.getQuantity()))
                .toList();

        return new CartDTO(cart.getId(), cart.getName(), itemDTOs);
    }

    public CartItemDTO getCartItemDTO(Product product, int quantity) {
        return new CartItemDTO(product, quantity);
    }



    // Xóa sản phẩm khỏi giỏ hàng
    public void removeCartItem(int cartId, int productId) {
        CartItem cartItem = cartItemRepository.findById(new CartItemId(cartId, productId)).orElseThrow(() -> new RuntimeException("Không có sản phẩm này trong giỏ"));
        cartItemRepository.deleteById(new CartItemId(cartItem.getCart().getId(), cartItem.getProduct().getId()));
    }

    public void saveCart(int cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Giỏ hàng không tồn tại!"));

        cartRepository.save(cart); // Không cần thiết nếu không thay đổi gì, có thể bỏ dòng này
    }
}
