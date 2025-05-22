document.addEventListener('DOMContentLoaded', () => {
    const messageDiv = document.getElementById('message');
    const otpInput = document.getElementById('otp');
    const newPasswordInput = document.getElementById('newPassword');

    // Get email from URL query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');

    if (!email) {
        messageDiv.textContent = 'Không tìm thấy email. Vui lòng quay lại trang quên mật khẩu';
        messageDiv.className = 'message error';
        return;
    }

    window.resetPassword = async () => {
        const otp = otpInput.value.trim();
        const newPassword = newPasswordInput.value.trim();

        if (!otp || !newPassword) {
            messageDiv.textContent = 'Vui lòng nhập đầy đủ mã OTP và mật khẩu mới';
            messageDiv.className = 'message error';
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/users/reset-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, otp, newPassword }),
            });

            if (response.ok) {
                messageDiv.textContent = 'Đặt lại mật khẩu thành công';
                messageDiv.className = 'message success';
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            } else {
                const error = await response.text();
                messageDiv.textContent = error || 'Không thể đặt lại mật khẩu';
                messageDiv.className = 'message error';
            }
        } catch (err) {
            console.error('Error:', err);
            messageDiv.textContent = 'Không thể kết nối đến server';
            messageDiv.className = 'message error';
        }
    };
});