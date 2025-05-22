loadAddEvent();

//CREATE CART LIST (đảm bảo sản phẩm ko bị reset sau khi load lại)
var cart = JSON.parse(localStorage.getItem("cart-list"));
if (cart != null) {
  cartItemList = cart;
} else {
  var cartItemList = [];
}

//thêm sản phẩm trang chi tiết sản phẩm
function themChiTietTaoQueen() {
  var title = "Táo Queen Organic New Zealand 1kg";
  var price = "158.000₫";
  var productImg = "../img/sp-nhap-khau-1.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}
function themChiTietTaoXanh() {
  var title = "Táo Xanh Grany Smith Hữu Cơ 1kg";
  var price = "139.000₫";
  var productImg = "../img/sp-nhap-khau-2.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}
function themChiTietDau() {
  var title = "Dâu Tuyết Hàn Quốc 330g";
  var price = "149.000₫";
  var productImg = "../img/sp-nhap-khau-3.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}
function themChiTietNho() {
  var title = "Việt quất New Zealand 1 hộp 125g";
  var price = "435.000₫";
  var productImg = "../img/sp-nhap-khau-4.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}
function themChiTietChuoi() {
  var title = "Chuối Laba Loại 1 Org 1kg";
  var price = "45.000₫";
  var productImg = "../img/sp-noi-dia-1.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}
function themChiTietSauRieng() {
  var title = "Sầu Riêng Chuồng Bò Nguyên Trái";
  var price = "260.000₫";
  var productImg = "../img/sp-noi-dia-2.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}
function themChiTietDua() {
  var title = "Dưa lưới biển Hằng Nga hữu cơ 1kg";
  var price = "120.000₫";
  var productImg = "../img/sp-noi-dia-3.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}
function themChiTietVai() {
  var title = "Vải U Trứng Phù Cừ 1kg";
  var price = "320.000₫";
  var productImg = "../img/sp-noi-dia-4.jpg";
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}

//bắt sự kiện của tất cả các nút Thêm các trong trang html
function loadAddEvent() {
  getSoLuongSp();
  //ADD TO CART
  var addCart = document.getElementsByClassName("add-cart");
  for (var i = 0; i < addCart.length; i++) {
    addCart[i].addEventListener("click", addCartClicked);
  }
}

//Bắt sự kiện của tất cả các nút Quantity
function loadQuantityEvent() {
  //QUANTITY CHANGES
  var quantityInputs = document.getElementsByClassName("cart-item-quantity");
  for (var i = 0; i < quantityInputs.length; i++) {
    var input = quantityInputs[i];
    input.addEventListener("change", quantityChanged);
  }
}

//LOAD DATA FOR CART PAGE
function loadDataCart() {
  getSoLuongSp();
  showCart();
  loadQuantityEvent();
  updatetotal();
  deleteItemCart();
}

//Xử lý sự kiện nút quantity khi bị thay đổi
function quantityChanged(event) {
  var input = event.target; //xác định vị trí của nút được bấm

  //ràng buộc giá trị nút quantity (khác null và bé hơn 0) là 1
  if (isNaN(input.value) || input.value <= 0) {
    input.value = 1;
  }
  //cập nhật tổng tiền
  updatetotal();
}
// Xử lý tổng tiền
function updatetotal() {
  var total = 0; //khai báo biến tổng tiền
  var cartContent = document.getElementsByClassName("cart-item-list")[0];
  var cartItems = cartContent.getElementsByClassName("cart-item");
  //chạy vòng lập cho mỗi item có trong cartItems
  for (var i = 0; i < cartItems.length; i++) {
    var cartItem = cartItems[i]; //lấy item
    var priceElement =
      cartItem.getElementsByClassName("cart-item-price")[0].innerText; //lấy dữ liệu giá
    console.log(priceElement);
    var quantityElement =
      cartItem.getElementsByClassName("cart-item-quantity")[0]; //lấy nút quantity
    var price = parseFloat(priceElement.replace("₫", "")); //xóa đi ký tự ₫ để thực hiện tính toán
    var quantity = quantityElement.value; //lấy giá trị nút quantity
    total = total + price * quantity; //thực hiện tính toán
  }
  total *= 1000;
  total = total.toFixed(3); //lấy 3 giá trị thập phân
  //format tiền VND
  let formatter = new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  });
  document.getElementsByClassName("total-price")[0].innerText =
    formatter.format(total);
}

//Xử lý sự kiện khi nhấn nút Thêm
function addCartClicked(event) {
  var button = event.target; //xác định vị trí nút bấm
  var shopProducts = button.parentElement.parentElement; //Chọn thẻ cha chứa các thẻ con có class bên dưới
  var title = shopProducts.getElementsByClassName("card-title")[0].innerText;
  var price = shopProducts.getElementsByClassName("card-text")[0].innerText;
  //Tiếp tục chọn thẻ cha chứa thẻ con có class car-img-top
  shopProducts = shopProducts.parentElement;
  var productImg = shopProducts.getElementsByClassName("card-img-top")[0].src;
  //tạo 1 biến chứa các thông tin
  var item = {
    ititle: title,
    iprice: price,
    iproductImg: productImg,
  };
  checkItem(item); //kiểm tra thông tin sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //lưu lại dữ liệu trong local Storage
  getSoLuongSp(); //cập nhật số lượng trên header
}

//Kiếm sản phẩm đã thêm vào giỏ hàng hay chưa
function checkItem(item) {
  var check = true;
  var cart = JSON.parse(localStorage.getItem("cart-list"));
  if (cart != null) {
    for (var i = 0; i < cart.length; i++) {
      //Nếu tìm thấy 1 sản phẩm trùng với tên của sản phẩm bắt sự kiện từ nút Thêm
      if (cart[i].ititle == item.ititle) {
        alert("Sản phẩm đã được thêm vào giỏ hàng!"); //Thông báo
        check = false; //Xác nhận dữ liệu đã thêm
        break;
      }
    }
  }
  //Nếu dữ liệu sản phẩm chưa được thêm
  if (check) {
    cartItemList.push(item); //thêm vào danh sách sản phẩm
    localStorage.setItem("cart-list", JSON.stringify(cartItemList)); //cập nhật lại dữ liệu trong localStorage
    getSoLuongSp(); //load lại số lượng sản phẩm trên Header
  }
}

//Lấy số lượng sản phẩm đã thêm trong giỏ hàng (cập nhật lên Header)
function getSoLuongSp() {
  var cart = JSON.parse(localStorage.getItem("cart-list"));
  if (cart != null) {
    document.getElementById("cart-item-quantity").innerHTML =
      "(" + cart.length + ")";
  }
}

//Hiển thị danh sách sản phẩm của Giỏ hàng
function showCart() {
  var cart = JSON.parse(localStorage.getItem("cart-list"));
  if (cart != null) {
    var cartItemContent = "";
    for (var i = 0; i < cart.length; i++) {
      //Thêm dữ liệu sản phẩm cho trang html
      cartItemContent += `<div class="cart-item p-3 row d-flex flex-row">
                  
                        <img class="col-2 img-fluid" src="${cart[i].iproductImg}" alt="" />
                        <div class="col-3 p-2 my-auto">
                            <h5 class="cart-item-title">${cart[i].ititle}</h5>
                        </div>
                        <div class="col-3 p-2 my-auto">
                            <p class="cart-item-price">${cart[i].iprice}</p>
                        </div>
                        <div class="col-4 p-2 my-auto d-flex justify-content-between algin-items-center">
                            <input type="number" value="1" class="cart-item-quantity text-center" style="width:4rem;">
                            <button class="btn btn-danger delete-button" type="button" style="width:7rem;">Xóa</button>
                    </div>
                </div>`;
    }
    document.getElementById("cart-information").innerHTML = cartItemContent; //gán dữ liệu
  }
}

//Bắt xự kiện của các nút delete
function deleteItemCart() {
  var deleteButton = document.getElementsByClassName("delete-button");
  for (var i = 0; i < deleteButton.length; i++) {
    deleteButton[i].addEventListener("click", deleteItem);
  }
}

//Xử lý sự kiện xóa
function deleteItem(event) {
  var button = event.target; // Lấy nút mà người dùng vừa nhấn
  var shopProduct = button.parentElement.parentElement; // Phần tử chứa thông tin sản phẩm
  var title =
    shopProduct.getElementsByClassName("cart-item-title")[0].innerText; // Lấy tiêu đề sản phẩm

  // Lấy danh sách giỏ hàng từ localStorage
  var cart = JSON.parse(localStorage.getItem("cart-list"));

  // Tìm sản phẩm cần xóa trong giỏ hàng dựa trên tiêu đề
  for (var i = 0; i < cart.length; i++) {
    if (cart[i].ititle === title) {
      cart.splice(i, 1); // Xóa sản phẩm tại vị trí tìm được
      break;
    }
  }

  // Cập nhật lại localStorage sau khi xóa sản phẩm
  localStorage.setItem("cart-list", JSON.stringify(cart));

  // Xóa phần tử HTML của sản phẩm khỏi trang
  shopProduct.remove();
  updatetotal();
  // Cập nhật lại số lượng sản phẩm trong giỏ hàng
  getSoLuongSp();
}
//Xóa trống Giỏ hàng
function deleteCart() {
  if (cartItemList.length != 0) {
    localStorage.removeItem("cart-list"); //xóa cart-list
    window.location = "../html/gio-hang.html"; //tải lại trang cart.html để load lại các dữ liệu
  } else {
    alert("Giỏ hàng hiện không có sản phẩm!");
  }
}
//Xử lý nút thanh toán

document.getElementById("payButton").addEventListener("click", function () {
  if (cartItemList.length != 0) {
    deleteCart();
    alert("Thanh toán thành công!");
  } else {
    alert("Không có sản phẩm trong giỏ hàng!");
  }
});
