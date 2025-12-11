package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.entity.Nhacungcap;
import K23cnt3.nht._2.service.NhacungcapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/nha-cung-cap")
public class NhaCungCapAdminController {

    @Autowired
    private NhacungcapService nhacungcapService;

    @GetMapping
    public String danhSachNhaCungCap(Model model) {
        List<Nhacungcap> nhacungcapList = nhacungcapService.getAllNhacungcap();
        model.addAttribute("nhacungcapList", nhacungcapList);
        return "admin/nhacungcap/list";
    }

    @GetMapping("/them")
    public String themNhaCungCapForm(Model model) {
        model.addAttribute("nhacungcap", new Nhacungcap());
        return "admin/nhacungcap/create";
    }

    @PostMapping("/luu")
    public String luuNhaCungCap(@ModelAttribute Nhacungcap nhacungcap,
                                RedirectAttributes redirectAttributes) {
        nhacungcapService.saveNhacungcap(nhacungcap);
        redirectAttributes.addFlashAttribute("success", "Lưu nhà cung cấp thành công!");
        return "redirect:/admin/nha-cung-cap";
    }

    @GetMapping("/sua/{id}")
    public String suaNhaCungCapForm(@PathVariable Integer id, Model model) {
        Nhacungcap nhacungcap = nhacungcapService.getNhacungcapById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp"));

        model.addAttribute("nhacungcap", nhacungcap);
        return "admin/nhacungcap/edit";
    }

    @GetMapping("/xoa/{id}")
    public String xoaNhaCungCap(@PathVariable Integer id,
                                RedirectAttributes redirectAttributes) {
        nhacungcapService.deleteNhacungcap(id);
        redirectAttributes.addFlashAttribute("success", "Xóa nhà cung cấp thành công!");
        return "redirect:/admin/nha-cung-cap";
    }
}