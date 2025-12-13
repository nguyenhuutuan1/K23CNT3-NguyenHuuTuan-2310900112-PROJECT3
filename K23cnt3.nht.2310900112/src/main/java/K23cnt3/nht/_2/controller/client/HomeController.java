package K23cnt3.nht._2.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/taphoa";
    }

    @GetMapping("/taphoa")
    public String home(Model model) {
        model.addAttribute("title", "Hệ thống quản lý tạp hóa NHT");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("studentId", "2310900112");
        model.addAttribute("studentName", "Nguyễn Hữu Tuấn");
        return "index";
    }

    @GetMapping("/taphoa/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Bảng điều khiển");
        return "dashboard";
    }
}