package vn.iuh.fit.se.productservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.iuh.fit.se.productservice.models.Product;
import vn.iuh.fit.se.productservice.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Lấy tất cả sản phẩm và trả về trang HTML
    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";  // Trả về file index.html
    }

    // Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> model.addAttribute("product", product));
        return "product_detail";  // Trả về trang chi tiết sản phẩm
    }

    // Hiển thị form thêm sản phẩm
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";  // Trả về trang form thêm sản phẩm
    }

    // Xử lý form thêm sản phẩm
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);  // Lưu sản phẩm vào cơ sở dữ liệu
        return "redirect:/products";  // Sau khi thêm, chuyển hướng về trang danh sách sản phẩm
    }
}
