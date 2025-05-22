// auth.js
class Auth {
    static getToken() {
        return localStorage.getItem('jwtToken');
    }

    static isAuthenticated() {
        const token = this.getToken();
        return !!token; // Trả về true nếu token tồn tại
    }

    static logout() {
        localStorage.removeItem('jwtToken');
        window.location.href = 'login.html';
    }

    static checkAuth() {
        const token = this.getToken();
        const messageDiv = document.getElementById('message');

        if (!token) {
            if (messageDiv) {
                messageDiv.textContent = 'Vui lòng đăng nhập để tiếp tục';
                messageDiv.className = 'message error';
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            }
            return false;
        }
        return true;
    }
}

// Sử dụng trong profile.js
document.addEventListener('DOMContentLoaded', async () => {
    if (!Auth.checkAuth()) return;

    const messageDiv = document.getElementById('message');
    const token = Auth.getToken();

    try {
        const response = await fetch('http://localhost:8080/users/profile', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const user = await response.json();
            document.getElementById('username').textContent = user.username || 'Chưa cập nhật';
            document.getElementById('email').textContent = user.email || 'Chưa cập nhật';
            document.getElementById('phoneNumber').textContent = user.phoneNumber || 'Chưa cập nhật';
        } else if (response.status === 401) {
            messageDiv.textContent = 'Phiên đăng nhập hết hạn, vui lòng đăng nhập lại';
            messageDiv.className = 'message error';
            Auth.logout();
        } else {
            const error = await response.text();
            messageDiv.textContent = error || 'Không thể lấy thông tin hồ sơ';
            messageDiv.className = 'message error';
        }
    } catch (err) {
        console.error('Fetch error:', err);
        if (messageDiv) {
            messageDiv.textContent = 'Không thể kết nối đến server';
            messageDiv.className = 'message error';
        }
    }
});

// Thêm nút đăng xuất
document.getElementById('logoutBtn')?.addEventListener('click', () => Auth.logout());