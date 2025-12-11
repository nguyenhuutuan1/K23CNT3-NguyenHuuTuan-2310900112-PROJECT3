package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.entity.Loaisanpham;
import K23cnt3.nht._2.entity.Nhacungcap;
import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.service.LoaisanphamService;
import K23cnt3.nht._2.service.NhacungcapService;
import K23cnt3.nht._2.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/san-pham")
public class SanPhamAdminController {

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private LoaisanphamService loaisanphamService;

    @Autowired
    private NhacungcapService nhacungcapService;

    @GetMapping
    public String danhSachSanPham(Model model) {
        List<Sanpham> sanphamList = sanphamService.getAllSanpham();
        model.addAttribute("sanphamList", sanphamList);
        return "admin/sanpham/list";
    }

    @GetMapping("/them")
    public String themSanPhamForm(Model model) {
        List<Loaisanpham> loaiList = loaisanphamService.getAllLoaisanpham();
        List<Nhacungcap> nccList = nhacungcapService.getAllNhacungcap();

        model.addAttribute("sanpham", new Sanpham());
        model.addAttribute("loaiList", loaiList);
        model.addAttribute("nccList", nccList);

        return "admin/sanpham/create";
    }

    @PostMapping("/luu")
    public String luuSanPham(@ModelAttribute Sanpham sanpham,
                             RedirectAttributes redirectAttributes) {
        sanphamService.saveSanpham(sanpham);
        redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công!");
        return "redirect:/admin/san-pham";
    }

    @GetMapping("/sua/{id}")
    public String suaSanPhamForm(@PathVariable Integer id, Model model) {
        Sanpham sanpham = sanphamService.getSanphamById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        List<Loaisanpham> loaiList = loaisanphamService.getAllLoaisanpham();
        List<Nhacungcap> nccList = nhacungcapService.getAllNhacungcap();

        model.addAttribute("sanpham", sanpham);
        model.addAttribute("loaiList", loaiList);
        model.addAttribute("nccList", nccList);

        return "admin/sanpham/edit";
    }

    @GetMapping("/xoa/{id}")
    public String xoaSanPham(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {
        sanphamService.deleteSanpham(id);
        redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        return "redirect:/admin/san-pham";
    }
}