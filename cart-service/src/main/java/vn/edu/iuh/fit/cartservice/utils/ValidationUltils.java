package vn.edu.iuh.fit.cartservice.utils;

public class ValidationUltils {
    public static boolean isValidInteger(String input) {
        try {
            int value = Integer.parseInt(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Số lượng không hợp lệ");
        }
    }
}
