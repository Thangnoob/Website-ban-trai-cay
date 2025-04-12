package vn.edu.iuh.fit.cartservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.iuh.fit.cartservice.model.CartItem;
import vn.edu.iuh.fit.cartservice.model.CartItemId;
import vn.edu.iuh.fit.cartservice.model.Product;
import vn.edu.iuh.fit.cartservice.repositories.ProductRepository;
import vn.edu.iuh.fit.cartservice.services.CartService;
import vn.edu.iuh.fit.cartservice.services.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam(required = false) Integer cartId,
                                       @RequestParam(required = false) Integer productId,
                                       @RequestParam(required = false) Integer quantity) {
        if (cartId != 1) {
            return ResponseEntity.badRequest().body("Không có giỏ hàng");
        }
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("Sản phẩm không hợp lệ");
        }

        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest().body("Số lượng không hợp lệ");
        }

        try {
            cartService.addToCart(cartId, productId, quantity);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
            return ResponseEntity.ok(cartService.getCartItemDTO(product, quantity));
//            CartItem cartItem = cartItemRepository.findById(new CartItemId(cartId, productId)).get();
//            return ResponseEntity.ok(cartService.getCartItemDTO(cartItem.getProduct(), cartItem.getQuantity()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestParam(required = false) Integer cartId,
                                        @RequestParam(required = false) Integer productId,
                                        @RequestParam(required = false) Integer quantity) {
        if (cartId != 1) {
            return ResponseEntity.badRequest().body("Không có giỏ hàng");
        }
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("Sản phẩm không hợp lệ");
        }

        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest().body("Số lượng không hợp lệ");
        }

        try {
            cartService.updateCartItem(cartId, productId, quantity);
            return ResponseEntity.ok("Cập nhật số lượng thành công.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/remove")
    public ResponseEntity<?> removeCartItem(@RequestParam(required = false) Integer cartId,
                                            @RequestParam(required = false) Integer productId) {
        if (cartId != 1) {
            return ResponseEntity.badRequest().body("Không có giỏ hàng");
        }
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("Sản phẩm không hợp lệ");
        }

        try {
            cartService.removeCartItem(cartId, productId);
            return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCart(@RequestParam int cartId) {
        try {
            cartService.saveCart(cartId);
            return ResponseEntity.ok("Lưu giỏ hàng thành công.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewCart(@RequestParam Integer cartId) {
        if (cartId != 1) {
            return ResponseEntity.badRequest().body("Không có giỏ hàng");
        }
        else return ResponseEntity.ok(cartService.getCartDTO(cartId));
    }

}
