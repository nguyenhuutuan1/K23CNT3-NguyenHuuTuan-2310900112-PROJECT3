package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.entity.Khachhang;
import K23cnt3.nht._2.service.KhachhangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/khach-hang")
public class KhachHangAdminController {

    @Autowired
    private KhachhangService khachhangService;

    @GetMapping
    public String danhSachKhachHang(Model model) {
        List<Khachhang> khachhangList = khachhangService.getAllKhachhang();
        model.addAttribute("khachhangList", khachhangList);
        return "admin/khachhang/list";
    }

    @GetMapping("/{id}")
    public String chiTietKhachHang(@PathVariable Integer id, Model model) {
        Khachhang khachhang = khachhangService.getKhachhangById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        model.addAttribute("khachhang", khachhang);
        return "admin/khachhang/detail";
    }

    @GetMapping("/xoa/{id}")
    public String xoaKhachHang(@PathVariable Integer id,
                               RedirectAttributes redirectAttributes) {
        khachhangService.deleteKhachhang(id);
        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
        return "redirect:/admin/khach-hang";
    }
}