package vn.edu.iuh.fit.cartservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.iuh.fit.cartservice.model.Cart;
import vn.edu.iuh.fit.cartservice.model.CartItem;
import vn.edu.iuh.fit.cartservice.model.CartItemId;
import vn.edu.iuh.fit.cartservice.model.Product;
import vn.edu.iuh.fit.cartservice.repositories.ProductRepository;
import vn.edu.iuh.fit.cartservice.services.CartService;
import vn.edu.iuh.fit.cartservice.services.ProductService;
import vn.edu.iuh.fit.cartservice.utils.ValidationUltils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createCart(@RequestParam(defaultValue = "Guest") String name) {
        try {
//            Cart cart = cartService.createCart(name);
            Cart cart = cartService.createCartToRedis(name);
            return ResponseEntity.ok("Tạo giỏ hàng thành công: \n" + cartService.getCartDTO(cart.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam(required = false) Integer cartId,
                                       @RequestParam(required = false) Integer productId,
                                       @RequestParam(required = false) Integer quantity,
                                       @RequestParam(defaultValue = "Guest") String name) {
        if (cartId == null || cartId <= 0) {
            return ResponseEntity.badRequest().body("Mã giở hàng không hợp lệ");
        }
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("Sản phẩm không hợp lệ");
        }

        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest().body("Số lượng không hợp lệ");
        }

        try {
            if (!cartService.cartExists(cartId) && cartService.productExists(productId)) {
                // Tạo giở hàng mới
//                Cart cart = cartService.createCart(name);
                Cart cart = cartService.createCartToRedis(name);

//                cartService.addToCart(cart.getId(), productId, quantity);
                cartService.addToCartRedis(cartId, productId, quantity);
                return ResponseEntity.ok("Không có giỏ hàng này. " +
                        "Đã tạo giỏ hàng cho khách hàng " + name + " (ID =  " + cart.getId() + "). " +
                        "Thêm sản phẩm thành công");
            } else {
                // Sử dụng giỏ hàng cũ
                Product product = cartService.getProduct(productId);
//                cartService.addToCart(cartId, productId, quantity);
                cartService.addToCartRedis(cartId, productId, quantity);
                return ResponseEntity.ok(cartService.getCartItemDTO(product, quantity));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestParam(required = false) Integer cartId,
                                        @RequestParam(required = false) Integer productId,
                                        @RequestParam(required = false) Integer quantity) {
        if (cartId == null || cartId <= 0) {
            return ResponseEntity.badRequest().body("Mã giở hàng không hợp lệ");
        }
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("Sản phẩm không hợp lệ");
        }

        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest().body("Số lượng không hợp lệ");
        }

        try {
//            cartService.updateCartItem(cartId, productId, quantity);
            cartService.updateCartItemRedis(cartId, productId, quantity);
            return ResponseEntity.ok("Cập nhật số lượng thành công.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/remove")
    public ResponseEntity<?> removeCartItem(@RequestParam(required = false) Integer cartId,
                                            @RequestParam(required = false) Integer productId) {
        if (cartId == null || cartId <= 0) {
            return ResponseEntity.badRequest().body("Mã giở hàng không hợp lệ");
        }
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("Sản phẩm không hợp lệ");
        }

        try {
//            cartService.removeCartItem(cartId, productId);

            cartService.removeCartItemRedis(cartId, productId);
            return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCart(@RequestParam(required = false) Integer cartId) {
        try {
            cartService.saveCartFromRedisToDatabase(cartId);
            return ResponseEntity.ok("Lưu giỏ hàng thành công.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Giỏ hàng không tồn tại");
        }
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewCart(@RequestParam(required = false) Integer cartId) {
        if (cartId == null || cartId <= 0) {
            return ResponseEntity.badRequest().body("Mã giở hàng không hợp lệ");
        }
        else if (!cartService.cartExists(cartId)) {
            return ResponseEntity.badRequest().body("Giỏ hàng không tồn tại");
        }
        else return ResponseEntity.ok(cartService.viewCartFromRedis(cartId));
    }

}
