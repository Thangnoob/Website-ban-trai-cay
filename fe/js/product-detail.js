$(document).ready(function () {
  // Lấy ID sản phẩm từ query string
  const urlParams = new URLSearchParams(window.location.search);
  const productId = urlParams.get("id");

  if (!productId) {
    $("#product-detail").html(
      '<p class="text-danger">Không tìm thấy ID sản phẩm.</p>'
    );
    return;
  }

  // Gửi yêu cầu AJAX để lấy chi tiết sản phẩm
  $.ajax({
    url: `http://localhost:9090/product/${productId}`,
    type: "GET",
    dataType: "json",
    success: function (product) {
      displayProductDetail(product);
    },
    error: function (xhr) {
      console.error(
        "Lỗi khi lấy chi tiết sản phẩm:",
        xhr.status,
        xhr.responseText
      );
      $("#product-detail").html(
        `<p class="text-danger">Không thể tải chi tiết sản phẩm: ${
          xhr.responseText || "Lỗi server"
        }.</p>`
      );
    },
  });

  // Hàm hiển thị chi tiết sản phẩm
  function displayProductDetail(product) {
    // Định dạng giá với dấu chấm
    const formattedPrice = product.price
      .toFixed(0)
      .replace(/\B(?=(\d{3})+(?!\d))/g, ".");

    // Xác định tình trạng
    const stockStatus = product.stockQuantity > 0 ? "Còn hàng" : "Hết hàng";

    // Cập nhật HTML
    const productHtml = `
      <div class="col-md-6">
        <img src="${
          product.imagePath || "../images/default-product.jpg"
        }" class="img-fluid" alt="${product.name}">
      </div>
      <div class="col-md-6">
        <h3>${product.name}</h3>
        <p>Thương hiệu: SuperFruits | Tình trạng: ${stockStatus}</p>
        <p>Đánh giá: <span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star"></span><span class="fa fa-star"></span></p>
        <p class="text-success h4">${formattedPrice}₫</p>
        <button class="btn btn-success add-cart" data-id="${product.id}" ${
      stockStatus === "Hết hàng" ? "disabled" : ""
    }>Thêm vào giỏ hàng</button>
      </div>
    `;
    $("#product-detail").html(productHtml);
  }

  // Xử lý sự kiện "Thêm vào giỏ hàng" (tạm thời)
  $(document).on("click", ".add-cart", function (e) {
    e.preventDefault();
    const productId = $(this).data("id");
    alert("Thêm sản phẩm ID " + productId + " vào giỏ hàng (chưa triển khai)");
    // TODO: Gọi API /reduceQuantity để giảm stockQuantity
  });
});
