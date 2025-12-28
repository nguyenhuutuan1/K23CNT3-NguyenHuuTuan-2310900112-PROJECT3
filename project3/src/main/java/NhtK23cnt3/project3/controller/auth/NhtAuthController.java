package NhtK23cnt3.project3.controller.auth;

import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.entity.user.NhtUserRole;
import NhtK23cnt3.project3.service.user.NhtUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NhtAuthController {

    private final NhtUserService userService;

    // ========== LOGIN ==========

    // Hiển thị form login (GET /login)
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "redirect", required = false) String redirect,
                                Model model) {
        model.addAttribute("redirect", redirect);
        return "auth/NhtLogin";
    }

    // Xử lý submit form login (POST /login)
    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          @RequestParam(value = "redirect", required = false) String redirect,
                          HttpSession session,
                          Model model) {

        NhtUser user = userService.login(email, password);
        if (user == null) {
            model.addAttribute("error", "Sai email hoặc mật khẩu");

            // giữ lại redirect khi login bị sai
            model.addAttribute("redirect", redirect);

            return "auth/NhtLogin";
        }

        // Save session
        session.setAttribute("currentUser", user);

        // Nếu có redirect → quay lại trang ban đầu
        if (redirect != null && !redirect.isEmpty()) {
            return "redirect:" + redirect;
        }

        // Điều hướng theo role (nếu không có redirect)
        if (user.getRole() == NhtUserRole.ADMIN) {
            return "redirect:/admin/dashboard";
        } else if (user.getRole() == NhtUserRole.SHIPPER) {
            return "redirect:/ship/orders";
        } else {
            return "redirect:/";
        }
    }

    // ========== REGISTER (CHO KHÁCH) ==========

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "auth/NhtRegister";   // file auth/NhtRegister.html
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String phone,
                             Model model) {

        NhtUser user = userService.registerCustomer(fullName, email, password, phone);
        if (user == null) {
            model.addAttribute("error", "Email đã tồn tại");
            return "auth/NhtRegister";
        }

        model.addAttribute("msg", "Đăng ký thành công, mời bạn đăng nhập");
        return "auth/NhtLogin";
    }

    // ========== LOGOUT ==========

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
