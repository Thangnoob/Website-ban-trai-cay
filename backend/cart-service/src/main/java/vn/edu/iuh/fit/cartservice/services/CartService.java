package vn.edu.iuh.fit.cartservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    private final RedisTemplate<String, Object> redisTemplate;


    // Tạo giỏ hàng mới và lưu rỗng vào Redis
    public Cart createCartToRedis(String name) {
        Cart cart = new Cart(name);
        cart = cartRepository.save(cart); // Lưu vào DB để lấy ID

        // Tạo giỏ hàng trống trong Redis
        redisTemplate.opsForHash().putAll("cart:" + cart.getId(), new HashMap<String, CartItemDTO>());
        return cart;
    }

    // Thêm sản phẩm vào Redis
    public void addToCartRedis(int cartId, int productId, int quantity) {
        String redisKey = "cart:" + cartId;
        String productKey = String.valueOf(productId);

        // Lấy sản phẩm từ Redis (nếu có)
        CartItemDTO existingItem = getOps().get(redisKey, productKey);

        int updatedQuantity = quantity;
        Product product = getProduct(productId);

        if (existingItem != null) {
            // Nếu đã tồn tại, cộng dồn số lượng
            updatedQuantity += existingItem.getQuantity();
        }

        CartItemDTO itemDTO = new CartItemDTO(product, updatedQuantity);
        getOps().put(redisKey, productKey, itemDTO);
    }

    // Cập nhật số lượng sản phẩm trong Redis
    public void updateCartItemRedis(int cartId, int productId, int quantity) {
        String redisKey = "cart:" + cartId;
        CartItemDTO itemDTO = getOps().get(redisKey, String.valueOf(productId));
        if (itemDTO != null) {
            itemDTO.setQuantity(quantity);
            getOps().put(redisKey, String.valueOf(productId), itemDTO);
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại trong Redis giỏ hàng.");
        }
    }

    // Xoá sản phẩm khỏi Redis
    public void removeCartItemRedis(int cartId, int productId) {
        String redisKey = "cart:" + cartId;

        // Kiểm tra xem Redis có chứa key sản phẩm này không
        Boolean exists = getOps().hasKey(redisKey, String.valueOf(productId));
        if (Boolean.TRUE.equals(exists)) {
            getOps().delete(redisKey, String.valueOf(productId));
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại trong Redis giỏ hàng.");
        }
    }


    // Lưu giỏ hàng từ Redis vào database
    public void saveCartFromRedisToDatabase(int cartId) {
        Cart cart = getCart(cartId);
        String redisKey = "cart:" + cartId;

        Map<String, CartItemDTO> redisItems = getOps().entries(redisKey);
        for (CartItem item : cart.getItems()) {
            // Kiểm tra xem CartItem đã tồn tại trong Redis chưa
            if (!redisItems.containsKey(item.getProduct().getId())) {
                cartItemRepository.delete(item);
            }
        }

        for (CartItemDTO dto : redisItems.values()) {
            Product product = getProduct(dto.getProduct().getId());

            Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cartId, product.getId());
            if (existingItem.isPresent()) {
                // Nếu tồn tại, cập nhật số lượng
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + dto.getQuantity());
                cartItemRepository.save(item);
            } else {
                // Nếu không tồn tại, tạo mới CartItem
                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(dto.getQuantity());
                cartItemRepository.save(item);
            }
        }

        redisTemplate.delete(redisKey);
    }
    // Xem giỏ hàng từ Redis
    public CartDTO viewCartFromRedis(int cartId) {
        String redisKey = "cart:" + cartId;
        Map<String, CartItemDTO> redisItems = getOps().entries(redisKey);

        List<CartItemDTO> items = redisItems.values().stream().toList();
        Cart cart = getCart(cartId);

        return new CartDTO(cartId, cart.getName(), items);
    }

    private HashOperations<String, String, CartItemDTO> getOps() {
        return redisTemplate.opsForHash();
    }

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
            existingItem.get().setQuantity(quantity);
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

        cartRepository.save(cart);
    }
}
