package K23cnt3.nht._2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Lấy thông tin lỗi
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exceptionType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        // Thêm thông tin vào model
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);
            model.addAttribute("statusText", HttpStatus.valueOf(statusCode).getReasonPhrase());

            // Xác định trang lỗi cụ thể
            if (statusCode == 404) {
                model.addAttribute("errorTitle", "Không tìm thấy trang");
                model.addAttribute("errorMessage", "Trang bạn đang tìm kiếm không tồn tại hoặc đã bị di chuyển.");
                return "error/404";
            } else if (statusCode == 403) {
                model.addAttribute("errorTitle", "Truy cập bị từ chối");
                model.addAttribute("errorMessage", "Bạn không có quyền truy cập vào trang này.");
                return "error/403";
            } else if (statusCode == 500) {
                model.addAttribute("errorTitle", "Lỗi máy chủ");
                model.addAttribute("errorMessage", "Đã xảy ra lỗi bên trong máy chủ. Vui lòng thử lại sau.");
                return "error/500";
            }
        }

        // Thêm các thông tin khác
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        } else {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi không xác định");
        }

        model.addAttribute("exceptionType", exceptionType != null ? exceptionType.toString() : "Không xác định");
        model.addAttribute("requestUri", requestUri != null ? requestUri.toString() : "Không xác định");
        model.addAttribute("errorTitle", "Lỗi Ứng dụng");

        // Trả về trang lỗi mặc định
        return "error/error";
    }

    public String getErrorPath() {
        return "/error";
    }
}