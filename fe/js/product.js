$(document).ready(function () {
  // Gửi yêu cầu AJAX để lấy danh sách sản phẩm
  $.ajax({
    url: "http://localhost:9090/product",
    type: "GET",
    dataType: "json",
    success: function (products) {
      displayProducts(products);
    },
    error: function (xhr) {
      console.error(
        "Lỗi khi lấy danh sách sản phẩm:",
        xhr.status,
        xhr.responseText
      );
      $("#product-list").html(
        '<p class="text-danger">Không thể tải danh sách sản phẩm. Vui lòng thử lại sau.</p>'
      );
    },
  });

  // Hàm hiển thị sản phẩm
  function displayProducts(products) {
    const $productList = $("#product-list");
    $productList.empty(); // Xóa nội dung cũ

    if (products.length === 0) {
      $productList.html("<p>Không có sản phẩm nào.</p>");
      return;
    }

    products.forEach(function (product) {
      // Định dạng giá với dấu chấm
      const formattedPrice = product.price
        .toFixed(0)
        .replace(/\B(?=(\d{3})+(?!\d))/g, ".");

      // Tạo HTML cho mỗi sản phẩm
      const productHtml = `
        <div class="col-md-3">
          <div class="card">
            <img src="${
              product.imagePath || "../images/default-product.jpg"
            }" class="card-img-top" alt="${product.name}">
            <div class="card-body">
              <h5 class="card-title">${product.name}</h5>
              <p class="card-text">${formattedPrice}₫</p>
              <div class="card-button">
                <a href="chi-tiet-sp-nhap-khau-dau.html?id=${
                  product.id
                }" class="btn btn-primary">Chi tiết</a>
                <a href="#" class="btn btn-primary add-cart" data-id="${
                  product.id
                }">Thêm vào giỏ hàng</a>
              </div>
            </div>
          </div>
        </div>
      `;
      $productList.append(productHtml);
    });
  }

  // Xử lý sự kiện "Thêm vào giỏ hàng" (tạm thời)
  $(document).on("click", ".add-cart", function (e) {
    e.preventDefault();
    const productId = $(this).data("id");
    alert("Thêm sản phẩm ID " + productId + " vào giỏ hàng (chưa triển khai)");
    // TODO: Thêm logic gửi yêu cầu tới backend để cập nhật giỏ hàng
  });
});
