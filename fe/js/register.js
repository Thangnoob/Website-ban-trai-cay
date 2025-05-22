// register.js
document.addEventListener('DOMContentLoaded', () => {
    if (Auth.isAuthenticated()) {
        window.location.href = 'profile.html'; // Nếu đã đăng nhập, chuyển hướng ngay
    }
});

document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const messageDiv = document.getElementById('message');
    messageDiv.textContent = '';

    const formData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        email: document.getElementById('email').value,
        phoneNumber: document.getElementById('phoneNumber').value,
    };

    try {
        const response = await fetch('http://localhost:8080/users/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData),
        });

        if (response.ok) {
            messageDiv.textContent = 'Đăng ký thành công!';
            messageDiv.className = 'message success';
            document.getElementById('registerForm').reset();
            window.location.href = 'login.html';
        } else {
            const error = await response.text();
            messageDiv.textContent = error || 'Đã có lỗi xảy ra';
            messageDiv.className = 'message error';
        }
    } catch (err) {
        console.error('Register error:', err);
        messageDiv.textContent = 'Không thể kết nối đến server';
        messageDiv.className = 'message error';
    }
});