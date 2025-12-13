package K23cnt3.nht._2.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Xóa hoặc comment phần AccessDeniedException nếu không dùng Spring Security
    /*
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex, Model model) {
        logger.error("Access denied: {}", ex.getMessage());
        model.addAttribute("errorCode", 403);
        model.addAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
        model.addAttribute("title", "Truy cập bị từ chối");
        return "error/403";
    }
    */

    // Thêm exception handler đơn giản hơn
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("Exception: ", ex);
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống: " + ex.getMessage());
        model.addAttribute("title", "Lỗi hệ thống");
        return "error/500";
    }
}