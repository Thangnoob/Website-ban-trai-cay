$(function () {
  $("#header").load("header.html", function () {
    updateHeader();
  });
  $("#footer").load("footer.html");

  function updateHeader() {
    const token = localStorage.getItem("jwtToken");
    const $authLinks = $("#auth-links");

    if (token) {
      $.ajax({
        url: "http://localhost:9090/user/profile",
        type: "GET",
        headers: { Authorization: "Bearer " + token },
        success: function (user) {
          $authLinks.empty();
          $authLinks.append(`
            <li class="nav-item">
              <a class="nav-link">Xin chào, ${user.username}</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" id="logout">Đăng xuất</a>
            </li>
          `);
          $("#logout").click(function (e) {
            e.preventDefault();
            localStorage.removeItem("jwtToken");
            updateHeader();
            window.location.href = "Dangnhap.html";
          });
        },
        error: function () {
          localStorage.removeItem("jwtToken");
          updateHeader();
        },
      });
    } else {
      $authLinks.empty();
      $authLinks.append(`
        <li class="nav-item">
          <a class="nav-link" href="Dangnhap.html">Đăng nhập</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="dangKyThanhVien.html">Đăng ký</a>
        </li>
      `);
    }
  }

  $("#registerForm").submit(function (e) {
    e.preventDefault();
    const registerData = {
      username: $("#username").val(),
      password: $("#password").val(),
      email: $("#email").val(),
      phoneNumber: $("#phoneNumber").val(),
    };

    $.ajax({
      url: "http://localhost:9090/user/register",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(registerData),
      success: function (response) {
        alert("Đăng ký thành công! Vui lòng đăng nhập.");
        window.location.href = "Dangnhap.html";
      },
      error: function (xhr) {
        console.error("Register error:", xhr.responseText);
        alert(
          "Lỗi đăng ký: " + (xhr.responseText || "Không thể kết nối đến server")
        );
      },
    });
  });

  $("#loginForm").submit(function (e) {
    e.preventDefault();
    const loginData = {
      email: $("#email").val(),
      password: $("#password").val(),
    };

    $.ajax({
      url: "http://localhost:9090/user/login",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(loginData),
      success: function (token) {
        localStorage.setItem("jwtToken", token);
        alert("Đăng nhập thành công!");
        window.location.href = "index.html";
      },
      error: function (xhr) {
        console.error("Login error:", xhr.status, xhr.responseText);
        alert(
          "Lỗi đăng nhập: " +
            (xhr.responseText || "Không thể kết nối đến server")
        );
      },
    });
  });
});
