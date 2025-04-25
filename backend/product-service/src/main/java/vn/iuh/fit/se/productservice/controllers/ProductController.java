package vn.iuh.fit.se.productservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.iuh.fit.se.productservice.models.Product;
import vn.iuh.fit.se.productservice.services.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;


//@Controller
//@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

//    @Autowired
    private final ProductService productService;

    // Lấy danh sách tất cả sản phẩm
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy sản phẩm với ID = " + id);
        }
    }

    // Thêm sản phẩm mới
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên sản phẩm không được để trống");
        }
        if (product.getPrice() <= 0) {
            return ResponseEntity.badRequest().body("Giá sản phẩm phải lớn hơn 0");
        }

        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok("Thêm sản phẩm thành công: ID = " + savedProduct.getId());
    }

    // Cập nhật sản phẩm
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProductOpt = productService.getProductById(id);
        if (existingProductOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm với ID = " + id);
        }

        Product existingProduct = existingProductOpt.get();
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImagePath(updatedProduct.getImagePath());

        productService.saveProduct(existingProduct);
        return ResponseEntity.ok("Cập nhật sản phẩm thành công");
    }

    // Xoá sản phẩm
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm với ID = " + id);
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok("Xoá sản phẩm thành công");
    }

    // Tìm kiếm theo tên
//    @GetMapping("/search")
//    public ResponseEntity<?> searchProductsByName(@RequestParam String name) {
//        List<Product> products = productService.searchProductsByName(name);
//        return ResponseEntity.ok(products);
//    }
    @PostMapping("/search")
    public ResponseEntity<?> searchProductsByNameInBody(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    // Lọc theo khoảng giá
//    @GetMapping("/filter")
//    public ResponseEntity<?> filterProductsByPrice(@RequestParam double minPrice, @RequestParam double maxPrice) {
//        if (minPrice < 0 || maxPrice < 0 || maxPrice < minPrice) {
//            return ResponseEntity.badRequest().body("Khoảng giá không hợp lệ");
//        }
//        List<Product> products = productService.filterProductsByPrice(minPrice, maxPrice);
//        return ResponseEntity.ok(products);
//    }
    @PostMapping("/filter")
    public ResponseEntity<?> filterProductsByPriceBody(@RequestBody Map<String, Double> body) {

        if (!body.containsKey("minPrice") || !body.containsKey("maxPrice")) {
            return ResponseEntity.badRequest().body("Phải nhập đầy đủ thông tin minPrice và maxPrice");
        }

        Object minPriceObj = body.get("minPrice");
        Object maxPriceObj = body.get("maxPrice");

        if (minPriceObj == null || maxPriceObj == null) {
            return ResponseEntity.badRequest().body("Phải nhập đầy đủ thông tin minPrice và maxPrice");
        }

        try {
            double minPrice = Double.parseDouble(minPriceObj.toString());
            double maxPrice = Double.parseDouble(maxPriceObj.toString());

            if (minPrice < 0 || maxPrice < 0 || maxPrice < minPrice) {
                return ResponseEntity.badRequest().body("Khoảng giá không hợp lệ");
            }

            List<Product> products = productService.filterProductsByPrice(minPrice, maxPrice);
            return ResponseEntity.ok(products);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Giá trị minPrice và maxPrice phải là số hợp lệ");
        }
    }
}
