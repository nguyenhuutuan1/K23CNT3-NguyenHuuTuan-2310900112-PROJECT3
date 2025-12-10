package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.service.HoadonService;
import K23cnt3.nht._2.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@Controller
public class HomeController {

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private HoadonService hoadonService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("title", "Trang chủ - Cửa hàng tạp hóa");
        model.addAttribute("sanphamList", sanphamService.getSanphamConHang());
        model.addAttribute("doanhThuHomNay", hoadonService.getDoanhThuNgay(LocalDate.now()));
        model.addAttribute("soHoaDonHomNay", hoadonService.countHoaDonNgay(LocalDate.now()));
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard - Thống kê");
        model.addAttribute("totalProducts", sanphamService.getAllSanpham().size());
        model.addAttribute("availableProducts", sanphamService.getSanphamConHang().size());
        return "dashboard";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Giới thiệu");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Liên hệ");
        return "contact";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
        model.addAttribute("title", "Đăng nhập");
        return "login";
    }
}