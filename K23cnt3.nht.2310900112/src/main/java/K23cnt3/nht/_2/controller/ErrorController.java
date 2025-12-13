package K23cnt3.nht._2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer status = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String message = (String) request.getAttribute("jakarta.servlet.error.message");
        String path = (String) request.getAttribute("jakarta.servlet.error.request_uri");

        if (status == null) status = 500;
        if (message == null) message = "Đã xảy ra lỗi không xác định";
        if (path == null) path = "Không xác định";

        HttpStatus httpStatus = HttpStatus.valueOf(status);
        String errorMsg;

        switch (status) {
            case 400 -> errorMsg = "Yêu cầu không hợp lệ";
            case 404 -> errorMsg = "Trang không tồn tại";
            case 403 -> errorMsg = "Không có quyền truy cập";
            case 500 -> errorMsg = "Lỗi máy chủ";
            default -> errorMsg = httpStatus.getReasonPhrase();
        }

        model.addAttribute("timestamp", new Date());
        model.addAttribute("status", status);
        model.addAttribute("error", httpStatus.getReasonPhrase());
        model.addAttribute("message", errorMsg);
        model.addAttribute("path", path);

        return "error";
    }
}