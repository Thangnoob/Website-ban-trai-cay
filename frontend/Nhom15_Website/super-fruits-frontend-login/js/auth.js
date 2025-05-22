// auth.js
class Auth {
    static getToken() {
        return localStorage.getItem('jwtToken');
    }

    static isAuthenticated() {
        const token = this.getToken();
        return !!token;
    }

    static logout() {
        localStorage.removeItem('jwtToken');
        window.location.href = 'login.html';
    }

    static async checkAuth(redirect = true) {
        const token = this.getToken();
        const messageDiv = document.getElementById('message');

        if (!token) {
            if (redirect && messageDiv) {
                messageDiv.textContent = 'Vui lòng đăng nhập để tiếp tục';
                messageDiv.className = 'message error';
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            }
            return false;
        }

        try {
            const response = await fetch('http://localhost:8080/users/profile', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                return true;
            } else if (response.status === 401) {
                if (redirect && messageDiv) {
                    messageDiv.textContent = 'Phiên đăng nhập hết hạn, vui lòng đăng nhập lại';
                    messageDiv.className = 'message error';
                    localStorage.removeItem('jwtToken');
                    setTimeout(() => {
                        window.location.href = 'login.html';
                    }, 2000);
                }
                return false;
            } else {
                if (redirect && messageDiv) {
                    messageDiv.textContent = 'Không thể xác minh trạng thái đăng nhập';
                    messageDiv.className = 'message error';
                }
                return false;
            }
        } catch (err) {
            console.error('Auth check error:', err);
            if (redirect && messageDiv) {
                messageDiv.textContent = 'Không thể kết nối đến server';
                messageDiv.className = 'message error';
            }
            return false;
        }
    }
}