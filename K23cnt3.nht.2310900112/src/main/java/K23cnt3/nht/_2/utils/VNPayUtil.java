package K23cnt3.nht._2.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VNPayUtil {

    // Cấu hình VNPay (trong thực tế nên lưu trong application.properties)
    private static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String TERMINAL_ID = "K23CNT3NHT";
    private static final String SECRET_KEY = "K23CNT3NHTPROJECT2024";
    private static final String VERSION = "2.1.0";
    private static final String COMMAND = "pay";
    private static final String CURRENCY = "VND";
    private static final String LOCALE = "vn";

    // Tạo URL thanh toán VNPay
    public static String createPaymentUrl(Integer orderId, Long amount, String orderInfo,
                                          String returnUrl, String ipAddress) {

        Map<String, String> vnpParams = new HashMap<>();

        // Thông tin bắt buộc
        vnpParams.put("vnp_Version", VERSION);
        vnpParams.put("vnp_Command", COMMAND);
        vnpParams.put("vnp_TmnCode", TERMINAL_ID);
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay yêu cầu nhân 100
        vnpParams.put("vnp_CurrCode", CURRENCY);
        vnpParams.put("vnp_TxnRef", generateTransactionRef(orderId));
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", LOCALE);
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_IpAddr", ipAddress);

        // Ngày giờ tạo giao dịch
        String createDate = getCurrentDateTime();
        vnpParams.put("vnp_CreateDate", createDate);

        // Thêm thời gian hết hạn (15 phút)
        String expireDate = getExpireDateTime();
        vnpParams.put("vnp_ExpireDate", expireDate);

        // Sắp xếp tham số theo thứ tự alphabet
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi dữ liệu
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                // Xây dựng chuỗi hash
                hashData.append(fieldName).append("=").append(fieldValue).append("&");

                // Xây dựng chuỗi query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8))
                        .append("&");
            }
        }

        // Xóa ký tự & cuối cùng
        hashData.deleteCharAt(hashData.length() - 1);
        query.deleteCharAt(query.length() - 1);

        // Tạo chữ ký
        String vnpSecureHash = hmacSHA512(SECRET_KEY, hashData.toString());

        // Thêm chữ ký vào URL
        String paymentUrl = VNPAY_URL + "?" + query + "&vnp_SecureHash=" + vnpSecureHash;

        return paymentUrl;
    }

    // Kiểm tra chữ ký từ VNPay callback
    public static boolean verifyReturnUrl(Map<String, String> params) {
        String vnpSecureHash = params.get("vnp_SecureHash");

        if (vnpSecureHash == null || vnpSecureHash.isEmpty()) {
            return false;
        }

        // Xóa tham số secure hash để tính toán
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        // Sắp xếp tham số
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi hash
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append("=").append(fieldValue).append("&");
            }
        }

        // Xóa ký tự & cuối cùng
        if (hashData.length() > 0) {
            hashData.deleteCharAt(hashData.length() - 1);
        }

        // Tính toán chữ ký
        String calculatedHash = hmacSHA512(SECRET_KEY, hashData.toString());

        // So sánh chữ ký
        return calculatedHash.equals(vnpSecureHash);
    }

    // Tạo mã giao dịch
    private static String generateTransactionRef(Integer orderId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        return "K23CNT3_" + orderId + "_" + timestamp;
    }

    // Lấy thời gian hiện tại định dạng VNPay
    private static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    // Lấy thời gian hết hạn (15 phút sau)
    private static String getExpireDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().plusMinutes(15).format(formatter);
    }

    // Hàm băm HMAC SHA512
    private static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Chuyển sang hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký VNPay", e);
        }
    }

    // Kiểm tra kết quả thanh toán
    public static boolean isPaymentSuccessful(String responseCode) {
        return "00".equals(responseCode);
    }

    // Lấy thông báo từ response code
    public static String getResponseMessage(String responseCode) {
        switch (responseCode) {
            case "00": return "Giao dịch thành công";
            case "07": return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
            case "09": return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
            case "10": return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11": return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "12": return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            case "13": return "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "24": return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            case "51": return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            case "65": return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            case "75": return "Ngân hàng thanh toán đang bảo trì.";
            case "79": return "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
            case "99": return "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)";
            default: return "Lỗi không xác định";
        }
    }
}