package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.entity.Nhanvien;
import K23cnt3.nht._2.service.NhanvienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/nhan-vien")
public class NhanVienAdminController {

    @Autowired
    private NhanvienService nhanvienService;

    @GetMapping
    public String danhSachNhanVien(Model model) {
        List<Nhanvien> nhanvienList = nhanvienService.getAllNhanvien();
        model.addAttribute("nhanvienList", nhanvienList);
        return "admin/nhanvien/list";
    }

    @GetMapping("/them")
    public String themNhanVienForm(Model model) {
        model.addAttribute("nhanvien", new Nhanvien());
        return "admin/nhanvien/create";
    }

    @PostMapping("/luu")
    public String luuNhanVien(@ModelAttribute Nhanvien nhanvien,
                              RedirectAttributes redirectAttributes) {
        nhanvienService.saveNhanvien(nhanvien);
        redirectAttributes.addFlashAttribute("success", "Lưu nhân viên thành công!");
        return "redirect:/admin/nhan-vien";
    }

    @GetMapping("/sua/{id}")
    public String suaNhanVienForm(@PathVariable Integer id, Model model) {
        Nhanvien nhanvien = nhanvienService.getNhanvienById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        model.addAttribute("nhanvien", nhanvien);
        return "admin/nhanvien/edit";
    }

    @GetMapping("/xoa/{id}")
    public String xoaNhanVien(@PathVariable Integer id,
                              RedirectAttributes redirectAttributes) {
        nhanvienService.deleteNhanvien(id);
        redirectAttributes.addFlashAttribute("success", "Xóa nhân viên thành công!");
        return "redirect:/admin/nhan-vien";
    }
}