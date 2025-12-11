package K23cnt3.nht._2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    // Cấu hình email (trong thực tế nên lưu trong application.properties)
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "webtaphoa.k23cnt3@gmail.com";
    private static final String EMAIL_PASSWORD = "your_password_here";
    private static final String EMAIL_FROM = "WebTạpHóa <webtaphoa.k23cnt3@gmail.com>";

    // Trong môi trường dev, chỉ log email thay vì gửi thật
    private static final boolean IS_DEV_MODE = true;

    // Gửi email đơn giản
    public static boolean sendEmail(String to, String subject, String content) {
        if (IS_DEV_MODE) {
            // Trong dev mode, chỉ log email
            logger.info("======= DEV MODE: EMAIL SIMULATION =======");
            logger.info("To: {}", to);
            logger.info("Subject: {}", subject);
            logger.info("Content: {}", content);
            logger.info("==========================================");
            return true;
        }

        // Trong production, gửi email thật
        return sendRealEmail(to, subject, content);
    }

    // Gửi email thông báo đơn hàng
    public static boolean sendOrderConfirmation(String to, String customerName,
                                                Integer orderId, Double totalAmount) {
        String subject = "Xác nhận đơn hàng #" + orderId;

        // Đọc template email từ file
        String template = loadEmailTemplate("order_confirmation.html");

        // Thay thế placeholder
        String content = template
                .replace("{{customerName}}", customerName)
                .replace("{{orderId}}", String.valueOf(orderId))
                .replace("{{totalAmount}}", String.format("%,.0f", totalAmount) + " ₫")
                .replace("{{orderDate}}", java.time.LocalDate.now().toString());

        return sendEmail(to, subject, content);
    }

    // Gửi email đăng ký tài khoản
    public static boolean sendRegistrationEmail(String to, String customerName) {
        String subject = "Chào mừng đến với WebTạpHóa";

        String template = loadEmailTemplate("welcome.html");
        String content = template.replace("{{customerName}}", customerName);

        return sendEmail(to, subject, content);
    }

    // Gửi email reset password
    public static boolean sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Yêu cầu đặt lại mật khẩu";

        String template = loadEmailTemplate("password_reset.html");
        String resetLink = "http://localhost:8080/tai-khoan/reset-password?token=" + resetToken;
        String content = template
                .replace("{{resetLink}}", resetLink)
                .replace("{{expireTime}}", "15 phút");

        return sendEmail(to, subject, content);
    }

    // Gửi email thật (sử dụng JavaMail)
    private static boolean sendRealEmail(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
            logger.info("Email sent successfully to: {}", to);
            return true;

        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", to, e);
            return false;
        }
    }

    // Đọc template email từ file
    private static String loadEmailTemplate(String templateName) {
        try {
            String templatePath = "src/main/resources/templates/email/" + templateName;
            StringBuilder content = new StringBuilder();

            BufferedReader reader = new BufferedReader(new FileReader(templatePath));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            return content.toString();

        } catch (IOException e) {
            logger.error("Failed to load email template: {}", templateName, e);

            // Trả về template mặc định
            return """
                   <!DOCTYPE html>
                   <html>
                   <head>
                       <meta charset="UTF-8">
                       <title>Email Template</title>
                   </head>
                   <body>
                       <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                           <h2>{{subject}}</h2>
                           <p>{{content}}</p>
                           <hr>
                           <p style="color: #666; font-size: 12px;">
                               Đây là email tự động từ hệ thống WebTạpHóa. Vui lòng không trả lời email này.
                           </p>
                       </div>
                   </body>
                   </html>
                   """;
        }
    }

    // Tạo token reset password
    public static String generateResetToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    // Kiểm tra email hợp lệ
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}