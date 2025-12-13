package K23cnt3.nht._2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    /**
     * Gửi email văn bản đơn giản (GIẢ LẬP)
     */
    public boolean sendSimpleEmail(String to, String subject, String content) {
        logger.info("📧 [EMAIL SIMULATION]");
        logger.info("To: {}", to);
        logger.info("Subject: {}", subject);
        logger.info("Content: {}", content);
        logger.info("✅ Email would be sent (simulation mode)");
        return true;
    }

    /**
     * Gửi email HTML (GIẢ LẬP)
     */
    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        logger.info("📧 [HTML EMAIL SIMULATION]");
        logger.info("To: {}", to);
        logger.info("Subject: {}", subject);
        logger.info("HTML Content (first 200 chars): {}",
                htmlContent.substring(0, Math.min(htmlContent.length(), 200)));
        logger.info("✅ HTML Email would be sent (simulation mode)");
        return true;
    }

    /**
     * Gửi email cho đơn hàng
     */
    public boolean sendOrderConfirmationEmail(String to, String customerName, String orderId, double totalAmount) {
        String subject = "Xác nhận đơn hàng #" + orderId + " - Cửa hàng tạp hóa NHT";
        String content = String.format(
                "Xin chào %s,\n\n" +
                        "Cảm ơn bạn đã đặt hàng tại cửa hàng tạp hóa NHT.\n" +
                        "Mã đơn hàng: %s\n" +
                        "Tổng tiền: %,.0f VNĐ\n\n" +
                        "Đơn hàng của bạn đang được xử lý.\n\n" +
                        "Trân trọng,\n" +
                        "Cửa hàng tạp hóa NHT\n" +
                        "Nguyễn Hữu Tuấn - 2310900112",
                customerName, orderId, totalAmount
        );

        return sendSimpleEmail(to, subject, content);
    }

    /**
     * Test email configuration
     */
    public String testEmailConfiguration() {
        return "✅ EmailUtil đang chạy ở chế độ giả lập.\n" +
                "Để gửi email thật, cấu hình trong application.properties:\n" +
                "spring.mail.host=smtp.gmail.com\n" +
                "spring.mail.username=your-email@gmail.com\n" +
                "spring.mail.password=your-app-password";
    }
}