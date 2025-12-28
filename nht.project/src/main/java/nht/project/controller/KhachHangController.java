package nht.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.model.KhachHang;
import nht.project.service.GioHangService;
import nht.project.service.KhachHangService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/khachhang")
@RequiredArgsConstructor
@Slf4j
public class KhachHangController {

    private final KhachHangService khachHangService;
    private final GioHangService gioHangService;

    /**
     * Trang đăng nhập
     */
    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        log.info("Truy cập trang đăng nhập");

        // Nếu đã đăng nhập, chuyển về trang chủ
        if (session.getAttribute("khachHang") != null) {
            return "redirect:/";
        }

        model.addAttribute("cartCount", gioHangService.demSoLuong(session));
        return "khachhang/login";
    }

    /**
     * Xử lý đăng nhập
     */
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String matKhau,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        log.info("Đăng nhập với email: {}", email);

        try {
            Optional<KhachHang> khachHangOpt = khachHangService.login(email, matKhau);

            if (khachHangOpt.isPresent()) {
                session.setAttribute("khachHang", khachHangOpt.get());
                redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");

                // Redirect về trang trước đó nếu có
                String returnUrl = (String) session.getAttribute("returnUrl");
                if (returnUrl != null) {
                    session.removeAttribute("returnUrl");
                    return "redirect:" + returnUrl;
                }

                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "Email hoặc mật khẩu không đúng");
                return "redirect:/khachhang/login";
            }

        } catch (Exception e) {
            log.error("Lỗi khi đăng nhập: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra. Vui lòng thử lại.");
            return "redirect:/khachhang/login";
        }
    }

    /**
     * Trang đăng ký
     */
    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        log.info("Truy cập trang đăng ký");

        // Nếu đã đăng nhập, chuyển về trang chủ
        if (session.getAttribute("khachHang") != null) {
            return "redirect:/";
        }

        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("cartCount", gioHangService.demSoLuong(session));
        return "khachhang/register";
    }

    /**
     * Xử lý đăng ký
     */
    @PostMapping("/register")
    public String register(@ModelAttribute KhachHang khachHang,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        log.info("Đăng ký tài khoản mới: {}", khachHang.getEmail());

        try {
            // Validate
            if (khachHang.getHoTen() == null || khachHang.getHoTen().trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập họ tên");
            }

            if (khachHang.getEmail() == null || khachHang.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập email");
            }

            if (khachHang.getMatKhau() == null || khachHang.getMatKhau().trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập mật khẩu");
            }

            if (khachHang.getMatKhau().length() < 6) {
                throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
            }

            if (khachHang.getSoDienThoai() == null || khachHang.getSoDienThoai().trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập số điện thoại");
            }

            // Đăng ký
            KhachHang registered = khachHangService.registerKhachHang(khachHang);

            // Tự động đăng nhập sau khi đăng ký
            session.setAttribute("khachHang", registered);

            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
            return "redirect:/";

        } catch (Exception e) {
            log.error("Lỗi khi đăng ký: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("khachHang", khachHang);
            return "redirect:/khachhang/register";
        }
    }

    /**
     * Đăng xuất
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("Đăng xuất");

        session.removeAttribute("khachHang");
        redirectAttributes.addFlashAttribute("success", "Đã đăng xuất");

        return "redirect:/";
    }

    /**
     * Trang thông tin cá nhân
     */
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        log.info("Xem thông tin cá nhân");

        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

        if (khachHang == null) {
            return "redirect:/khachhang/login";
        }

        // Lấy thông tin mới nhất từ DB
        khachHang = khachHangService.getKhachHangById(khachHang.getId())
                .orElse(khachHang);

        model.addAttribute("khachHang", khachHang);
        model.addAttribute("cartCount", gioHangService.demSoLuong(session));

        return "khachhang/profile";
    }

    /**
     * Cập nhật thông tin cá nhân
     */
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute KhachHang khachHangUpdate,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        log.info("Cập nhật thông tin cá nhân");

        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

        if (khachHang == null) {
            return "redirect:/khachhang/login";
        }

        try {
            // Giữ nguyên mật khẩu
            khachHangUpdate.setMatKhau(khachHang.getMatKhau());

            KhachHang updated = khachHangService.updateKhachHang(khachHang.getId(), khachHangUpdate);

            // Cập nhật session
            session.setAttribute("khachHang", updated);

            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công");

        } catch (Exception e) {
            log.error("Lỗi khi cập nhật thông tin: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/khachhang/profile";
    }

    /**
     * Đổi mật khẩu
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        log.info("Đổi mật khẩu");

        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

        if (khachHang == null) {
            return "redirect:/khachhang/login";
        }

        try {
            // Validate
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập mật khẩu mới");
            }

            if (newPassword.length() < 6) {
                throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
            }

            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
            }

            // Đổi mật khẩu
            khachHangService.changePassword(khachHang.getId(), oldPassword, newPassword);

            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");

        } catch (Exception e) {
            log.error("Lỗi khi đổi mật khẩu: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/khachhang/profile";
    }
}