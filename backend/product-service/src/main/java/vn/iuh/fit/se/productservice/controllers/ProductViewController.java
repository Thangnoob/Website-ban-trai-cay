package vn.iuh.fit.se.productservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iuh.fit.se.productservice.models.Product;
import vn.iuh.fit.se.productservice.services.ProductService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/products")
public class ProductViewController {

    private final ProductService productService;

    // Hiển thị trang danh sách sản phẩm
    @GetMapping
    public String getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {

        List<Product> products;

        // Tìm kiếm theo tên
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProductsByName(keyword);
        }
        // Lọc theo giá
        else if (minPrice != null || maxPrice != null) {
            double min = (minPrice != null) ? minPrice : 0;
            double max = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;
            products = productService.filterProductsByPrice(min, max);
        }
        // Lấy tất cả
        else {
            products = productService.getAllProducts();
        }

        // Truyền tham số tìm kiếm/lọc để hiển thị lại trên giao diện
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "index";
    }

    // Hiển thị trang chi tiết sản phẩm
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product_detail";
        } else {
            model.addAttribute("error", "Không tìm thấy sản phẩm với ID = " + id);
            return "error";
        }
    }

    // Hiển thị trang thêm sản phẩm

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";
    }
//    @PostMapping("/add")
//    public String addProduct(@RequestParam("name") String name,
//                             @RequestParam("price") double price,
//                             @RequestParam("imageFile") MultipartFile imageFile,
//                             Model model) {
//        try {
//            // Thư mục lưu file ảnh
//            String uploadDir = "uploads/images/";
//            File uploadPath = new File(uploadDir);
//
//            if (!uploadPath.exists()) {
//                uploadPath.mkdirs();
//            }
//
//            // Lưu file vào thư mục
//            String fileName = imageFile.getOriginalFilename();
//            Path filePath = Paths.get(uploadDir, fileName);
//            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            // Tạo đối tượng sản phẩm và lưu thông tin
//            Product product = new Product();
//            product.setName(name);
//            product.setPrice(price);
//            product.setImagePath("images/" + fileName); // chỉ lưu phần path tương đối để truy cập
//
//            productService.saveProduct(product);
//
//            return "redirect:/view/products";
//
//        } catch (IOException e) {
//            model.addAttribute("error", "Lỗi khi upload hình ảnh: " + e.getMessage());
//            return "add_product"; // Trả về form nếu lỗi
//        }
//    }

    @PostMapping("/add")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("price") double price,
                             @RequestParam("image") MultipartFile imageFile,
                             Model model) {

        try {
            if (imageFile.isEmpty()) {
                model.addAttribute("error", "Bạn phải chọn một hình ảnh.");
                return "add_product";
            }

            // Giả sử bạn lưu vào thư mục cục bộ (ví dụ "uploads")
            String uploadDir = "uploads";
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu file ảnh
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Tạo đối tượng Product
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setImagePath("/" + uploadDir + fileName); // Đường dẫn ảnh sau này bạn phải cấu hình để load từ static folder

            productService.saveProduct(product);
            return "redirect:/view/products";

        } catch (IOException e) {
            model.addAttribute("error", "Lỗi khi tải ảnh lên: " + e.getMessage());
            return "add_product";
        }
    }


    //    @GetMapping("/add")
//    public String showAddProductForm(Model model) {
//        model.addAttribute("product", new Product());
//        return "add_product";
//    }
//
//    // Xử lý thêm sản phẩm
//    @PostMapping("/add")
//    public String addProduct(@ModelAttribute Product product, Model model) {
//        if (product.getName() == null || product.getName().trim().isEmpty()) {
//            model.addAttribute("error", "Tên sản phẩm không được để trống");
//            return "add_product";
//        }
//        if (product.getPrice() <= 0) {
//            model.addAttribute("error", "Giá sản phẩm phải lớn hơn 0");
//            return "add_product";
//        }
//
//        productService.saveProduct(product);
//        return "redirect:/view/products"; // Điều chỉnh redirect
//    }
    // Hiển thị form cập nhật sản phẩm
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "update_product"; // Trỏ đến template update_product.html
        } else {
            model.addAttribute("error", "Không tìm thấy sản phẩm với ID = " + id);
            return "error";
        }
    }

    // Xử lý cập nhật sản phẩm
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product updatedProduct,
                                Model model) {
        Optional<Product> existingProductOpt = productService.getProductById(id);
        if (existingProductOpt.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy sản phẩm với ID = " + id);
            return "error";
        }

        // Validate dữ liệu
        if (updatedProduct.getName() == null || updatedProduct.getName().trim().isEmpty()) {
            model.addAttribute("error", "Tên sản phẩm không được để trống");
            return "update_product";
        }
        if (updatedProduct.getPrice() <= 0) {
            model.addAttribute("error", "Giá sản phẩm phải lớn hơn 0");
            return "update_product";
        }

        // Cập nhật thông tin
        Product existingProduct = existingProductOpt.get();
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImagePath(updatedProduct.getImagePath());

        productService.saveProduct(existingProduct);
        return "redirect:/view/products/" + id; // Chuyển hướng đến trang chi tiết
    }

    // Hiển thị trang tìm kiếm
    @GetMapping("/search")
    public String showSearchForm() {
        return "search";
    }

    // Xử lý tìm kiếm
    @PostMapping("/search")
    public String searchProductsByName(@RequestParam String name, Model model) {
        List<Product> products = productService.searchProductsByName(name);
        model.addAttribute("products", products);
        model.addAttribute("searchName", name); // Thêm tên tìm kiếm vào model
        return "search_results";
    }

    // Hiển thị trang lọc giá
    @GetMapping("/filter")
    public String showFilterForm() {
        return "filter";
    }

    // Xử lý lọc giá
    @PostMapping("/filter")
    public String filterProductsByPrice(@RequestParam double minPrice,
                                        @RequestParam double maxPrice,
                                        Model model) {
        if (minPrice < 0 || maxPrice < 0 || maxPrice < minPrice) {
            model.addAttribute("error", "Khoảng giá không hợp lệ");
            return "filter";
        }
        List<Product> products = productService.filterProductsByPrice(minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "filter_results";
    }

    // Xử lý xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/view/products"; // Điều chỉnh redirect
    }
}