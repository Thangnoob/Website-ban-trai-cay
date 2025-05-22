document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const messageDiv = document.getElementById("message");
  messageDiv.textContent = "";

  const formData = {
    email: document.getElementById("email").value,
    password: document.getElementById("password").value,
  };

  try {
    const response = await fetch("http://localhost:9090/users/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    });

    if (response.ok) {
      const token = await response.text();
      localStorage.setItem("jwtToken", token);
      messageDiv.textContent = "Đăng nhập thành công!";
      messageDiv.className = "message success";
      document.getElementById("loginForm").reset();
      window.location.href = "profile.html"; // Chuyển hướng sau khi đăng nhập thành công
    } else {
      const error = await response.text();
      messageDiv.textContent = error || "Đã có lỗi xảy ra";
      messageDiv.className = "message error";
    }
  } catch (err) {
    console.error("Login error:", err);
    messageDiv.textContent = "Không thể kết nối đến server";
    messageDiv.className = "message error";
  }
});

// Nếu đã đăng nhập, hiển thị thông báo thay vì chuyển hướng tự động
document.addEventListener("DOMContentLoaded", async () => {
  const messageDiv = document.getElementById("message");
  if (await Auth.checkAuth(false)) {
    // Không chuyển hướng tự động
    messageDiv.innerHTML =
      'Bạn đã đăng nhập. <a href="profile.html">Đi đến trang hồ sơ</a>';
    messageDiv.className = "message success";
    document.getElementById("loginForm").style.display = "none"; // Ẩn form đăng nhập
  }
});
