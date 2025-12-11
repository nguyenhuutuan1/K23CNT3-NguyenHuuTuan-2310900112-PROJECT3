package K23cnt3.nht._2.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

public class Helper {

    // Định dạng tiền Việt Nam
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 ₫";
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " ₫";
    }

    public static String formatCurrency(Double amount) {
        if (amount == null) {
            return "0 ₫";
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " ₫";
    }

    public static String formatCurrency(Integer amount) {
        if (amount == null) {
            return "0 ₫";
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " ₫";
    }

    // Định dạng số
    public static String formatNumber(Number number) {
        if (number == null) {
            return "0";
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(number);
    }

    // Định dạng ngày tháng
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    // Chuyển đổi tiếng Việt có dấu thành không dấu
    public static String removeDiacritics(String str) {
        if (str == null) {
            return "";
        }

        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }

    // Tạo slug từ tiêu đề
    public static String generateSlug(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "";
        }

        // Xóa dấu
        String slug = removeDiacritics(title);

        // Chuyển thành chữ thường
        slug = slug.toLowerCase();

        // Thay thế khoảng trắng và ký tự đặc biệt bằng dấu gạch ngang
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");
        slug = slug.replaceAll("\\s+", "-");
        slug = slug.replaceAll("-+", "-");
        slug = slug.trim();

        return slug;
    }

    // Rút gọn chuỗi với độ dài tối đa
    public static String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }

        if (str.length() <= maxLength) {
            return str;
        }

        return str.substring(0, maxLength - 3) + "...";
    }

    // Tính phần trăm giảm giá
    public static Double calculateDiscountPercentage(BigDecimal originalPrice, BigDecimal salePrice) {
        if (originalPrice == null || salePrice == null ||
                originalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        BigDecimal discount = originalPrice.subtract(salePrice);
        BigDecimal percentage = discount.multiply(BigDecimal.valueOf(100))
                .divide(originalPrice, 2, BigDecimal.ROUND_HALF_UP);

        return percentage.doubleValue();
    }

    // Kiểm tra số điện thoại Việt Nam
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        // Loại bỏ khoảng trắng và dấu +
        phone = phone.replaceAll("\\s+", "").replace("+84", "0");

        // Regex cho số điện thoại Việt Nam
        String phoneRegex = "^(0[3|5|7|8|9])[0-9]{8}$";
        return phone.matches(phoneRegex);
    }

    // Format số điện thoại
    public static String formatPhoneNumber(String phone) {
        if (!isValidPhoneNumber(phone)) {
            return phone;
        }

        phone = phone.replaceAll("\\s+", "").replace("+84", "0");

        if (phone.length() == 10) {
            return phone.substring(0, 4) + " " +
                    phone.substring(4, 7) + " " +
                    phone.substring(7);
        }

        return phone;
    }

    // Tạo mã đơn hàng
    public static String generateOrderCode(Integer orderId) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        return "DH" + timestamp + String.format("%04d", orderId);
    }

    // Tính khoảng cách ngày
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }

        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    // Chuyển đổi boolean sang Yes/No
    public static String booleanToYesNo(Boolean value) {
        if (value == null) {
            return "Không";
        }

        return value ? "Có" : "Không";
    }

    // Lấy initials từ tên
    public static String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "NN";
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 0) {
            return "NN";
        }

        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
        }

        return initials.toString().toUpperCase();
    }
}