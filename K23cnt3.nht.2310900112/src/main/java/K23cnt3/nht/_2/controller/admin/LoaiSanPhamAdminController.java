package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.entity.Loaisanpham;
import K23cnt3.nht._2.service.LoaisanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/loai-san-pham")
public class LoaiSanPhamAdminController {

    @Autowired
    private LoaisanphamService loaisanphamService;

    @GetMapping
    public String danhSachLoaiSanPham(Model model) {
        List<Loaisanpham> loaisanphamList = loaisanphamService.getAllLoaisanpham();
        model.addAttribute("loaisanphamList", loaisanphamList);
        return "admin/loaisanpham/list";
    }

    @GetMapping("/them")
    public String themLoaiSanPhamForm(Model model) {
        model.addAttribute("loaisanpham", new Loaisanpham());
        return "admin/loaisanpham/create";
    }

    @PostMapping("/luu")
    public String luuLoaiSanPham(@ModelAttribute Loaisanpham loaisanpham,
                                 RedirectAttributes redirectAttributes) {
        loaisanphamService.saveLoaisanpham(loaisanpham);
        redirectAttributes.addFlashAttribute("success", "Lưu loại sản phẩm thành công!");
        return "redirect:/admin/loai-san-pham";
    }

    @GetMapping("/sua/{id}")
    public String suaLoaiSanPhamForm(@PathVariable Integer id, Model model) {
        Loaisanpham loaisanpham = loaisanphamService.getLoaisanphamById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));

        model.addAttribute("loaisanpham", loaisanpham);
        return "admin/loaisanpham/edit";
    }

    @GetMapping("/xoa/{id}")
    public String xoaLoaiSanPham(@PathVariable Integer id,
                                 RedirectAttributes redirectAttributes) {
        loaisanphamService.deleteLoaisanpham(id);
        redirectAttributes.addFlashAttribute("success", "Xóa loại sản phẩm thành công!");
        return "redirect:/admin/loai-san-pham";
    }
}