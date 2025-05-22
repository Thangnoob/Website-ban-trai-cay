package vn.edu.iuh.fit.cartservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.cartservice.services.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartViewController {
    private final CartService cartService;

    @GetMapping("/viewPage")
    public String viewCartPage(@RequestParam("cartId") Integer cartId, Model model) {
        if (cartId == null || cartId <= 0 || !cartService.cartExists(cartId)) {
            model.addAttribute("errorMessage", "Giỏ hàng không tồn tại hoặc mã không hợp lệ.");
            return "cart"; // Trả về view với thông báo lỗi
        }

        model.addAttribute("cart", cartService.viewCartFromRedis(cartId));
        return "cart"; // Tên file cart.html trong templates
    }

    @PostMapping("/addPage")
    public String addToCart(@RequestParam Integer cartId,
                            @RequestParam Integer productId,
                            @RequestParam Integer quantity,
                            Model model) {
        cartService.addToCartRedis(cartId, productId, quantity);
        return "redirect:/cart/viewPage?cartId=" + cartId;
    }

    @PostMapping("/updatePage")
    public String updateCart(@RequestParam Integer cartId,
                             @RequestParam Integer productId,
                             @RequestParam Integer quantity) {
        cartService.updateCartItemRedis(cartId, productId, quantity);
        return "redirect:/cart/viewPage?cartId=" + cartId;
    }

    @PostMapping("/removePage")
    public String removeCart(@RequestParam Integer cartId,
                             @RequestParam Integer productId) {
        cartService.removeCartItemRedis(cartId, productId);
        return "redirect:/cart/viewPage?cartId=" + cartId;
    }

    @PostMapping("/savePage")
    public String saveCart(@RequestParam Integer cartId, Model model) {
        cartService.saveCartFromRedisToDatabase(cartId);
        model.addAttribute("successMessage", "Lưu giỏ hàng thành công!");
        return "redirect:/cart/viewPage?cartId=" + cartId;
    }
}
