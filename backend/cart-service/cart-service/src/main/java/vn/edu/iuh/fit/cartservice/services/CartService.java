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
    private final ProductService productService;

    public Cart getCart(int cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
    }

    public Product getProduct(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
    }

    public CartItem getCartItem(int cartId, int productId) {
        return cartItemRepository.findById(new CartItemId(cartId, productId)).orElseThrow(() -> new RuntimeException("Không có sản phẩm này trong giỏ"));
    }

    public boolean cartExists(int cartId) {
        return cartRepository.existsById(cartId);
    }

    public boolean productExists(int productId) {
        return productRepository.existsById(productId);
    }

    public Cart createCart(String name) {
        Cart cart = new Cart(name);
        cart = cartRepository.save(cart); // Tự động sinh ID

        return cart;
    }

    public void addToCart(int cartId, int productId, int quantity) {
        Cart cart = getCart(cartId);
        Product product = getProduct(productId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct() == product)
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
        Cart cart = getCart(cartId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            cartItemRepository.save(existingItem.get());
        } else {
            throw new RuntimeException("Sản phẩm chưa có trong giỏ hàng, không thể cập nhật");
        }
    }

    public CartDTO getCartDTO(int cartId) {
        Cart cart = getCart(cartId);

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(item.getProduct(), item.getQuantity()))
                .toList();

        return new CartDTO(cart.getId(), cart.getName(), itemDTOs);
    }

    public CartItemDTO getCartItemDTO(Product product, int quantity) {
        return new CartItemDTO(product, quantity);
    }

    public void removeCartItem(int cartId, int productId) {
        CartItem cartItem = getCartItem(cartId, productId);
        cartItemRepository.deleteById(new CartItemId(cartItem.getCart().getId(), cartItem.getProduct().getId()));
    }

    public void saveCart(int cartId) {
        Cart cart = getCart(cartId);

        cartRepository.save(cart); // Không cần thiết nếu không thay đổi gì, có thể bỏ dòng này
    }
}
