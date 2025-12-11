package K23cnt3.nht._2.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Xử lý 404 - Không tìm thấy trang
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, HttpServletRequest request, Model model) {
        logger.error("404 Error: {} {}", request.getMethod(), request.getRequestURI(), ex);

        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "Trang bạn tìm kiếm không tồn tại.");
        model.addAttribute("requestUrl", request.getRequestURI());

        return "error/404";
    }

    // Xử lý SanPhamNotFoundException
    @ExceptionHandler(SanPhamNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFound(SanPhamNotFoundException ex, Model model) {
        logger.error("Product not found: {}", ex.getMessage());

        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("title", "Không tìm thấy sản phẩm");

        return "error/custom";
    }

    // Xử lý UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        logger.error("User not found: {}", ex.getMessage());

        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("title", "Không tìm thấy người dùng");

        return "error/custom";
    }

    // Xử lý CartException
    @ExceptionHandler(CartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCartException(CartException ex, Model model) {
        logger.error("Cart error: {}", ex.getMessage());

        model.addAttribute("errorCode", 400);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("title", "Lỗi giỏ hàng");

        return "error/custom";
    }

    // Xử lý các exception khác (500)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        logger.error("500 Error: {} {}", request.getMethod(), request.getRequestURI(), ex);

        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
        model.addAttribute("requestUrl", request.getRequestURI());

        // Trong môi trường dev, hiển thị chi tiết lỗi
        if (isDevEnvironment()) {
            model.addAttribute("exception", ex.getClass().getName());
            model.addAttribute("exceptionMessage", ex.getMessage());
            model.addAttribute("stackTrace", ex.getStackTrace());
        }

        return "error/500";
    }

    // Kiểm tra môi trường dev
    private boolean isDevEnvironment() {
        String env = System.getProperty("spring.profiles.active");
        return env != null && (env.equals("dev") || env.equals("development"));
    }

    // Xử lý lỗi validation
    @ExceptionHandler(org.springframework.validation.BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(org.springframework.validation.BindException ex, Model model) {
        logger.error("Validation error: {}", ex.getMessage());

        StringBuilder errorMessage = new StringBuilder("Dữ liệu không hợp lệ: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");
        });

        model.addAttribute("errorCode", 400);
        model.addAttribute("errorMessage", errorMessage.toString());
        model.addAttribute("title", "Lỗi xác thực dữ liệu");

        return "error/custom";
    }

    // Xử lý lỗi truy cập bị từ chối (403)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex,
                                              Model model) {
        logger.error("Access denied: {}", ex.getMessage());

        model.addAttribute("errorCode", 403);
        model.addAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
        model.addAttribute("title", "Truy cập bị từ chối");

        return "error/403";
    }
}