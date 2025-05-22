document.addEventListener('DOMContentLoaded', () => {
    const messageDiv = document.getElementById('message');
    const emailInput = document.getElementById('email');

    window.requestOtp = async () => {
        const email = emailInput.value.trim();
        if (!email) {
            messageDiv.textContent = 'Vui lòng nhập email';
            messageDiv.className = 'message error';
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/users/forgot-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email }),
            });

            if (response.ok) {
                messageDiv.textContent = 'Mã OTP đã được gửi đến email của bạn';
                messageDiv.className = 'message success';
                setTimeout(() => {
                    window.location.href = 'reset-password.html?email=' + encodeURIComponent(email);
                }, 2000);
            } else {
                const error = await response.text();
                messageDiv.textContent = error || 'Không thể gửi mã OTP';
                messageDiv.className = 'message error';
            }
        } catch (err) {
            console.error('Error:', err);
            messageDiv.textContent = 'Không thể kết nối đến server';
            messageDiv.className = 'message error';
        }
    };
});