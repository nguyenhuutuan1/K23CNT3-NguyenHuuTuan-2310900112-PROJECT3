package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.entity.Khachhang;
import K23cnt3.nht._2.service.KhachhangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/khachhang")
public class KhachhangController {

    @Autowired
    private KhachhangService khachhangService;

    @GetMapping
    public String getAllKhachhang(Model model) {
        List<Khachhang> khachhangList = khachhangService.getAllKhachhang();
        model.addAttribute("khachhangList", khachhangList);
        model.addAttribute("title", "Danah sách khách hàng");
        return "khachhang/list";
    }

    @GetMapping("/{id}")
    public String getKhachhangById(@PathVariable Integer id, Model model) {
        Khachhang khachhang = khachhangService.getKhachhangById(id);
        model.addAttribute("khachhang", khachhang);
        model.addAttribute("title", "Chi tiết khách hàng: " + khachhang.getHoTen());
        return "khachhang/detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("khachhang", new Khachhang());
        model.addAttribute("title", "Thêm khách hàng mới");
        return "khachhang/form";
    }

    @PostMapping("/save")
    public String saveKhachhang(@ModelAttribute Khachhang khachhang,
                                RedirectAttributes redirectAttributes) {
        khachhangService.saveKhachhang(khachhang);
        redirectAttributes.addFlashAttribute("success", "Lưu khách hàng thành công!");
        return "redirect:/khachhang";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Khachhang khachhang = khachhangService.getKhachhangById(id);
        model.addAttribute("khachhang", khachhang);
        model.addAttribute("title", "Chỉnh sửa khách hàng: " + khachhang.getHoTen());
        return "khachhang/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteKhachhang(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        khachhangService.deleteKhachhang(id);
        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
        return "redirect:/khachhang";
    }

    @GetMapping("/search")
    public String searchKhachhang(@RequestParam String keyword, Model model) {
        List<Khachhang> khachhangList = khachhangService.searchKhachhang(keyword);
        model.addAttribute("khachhangList", khachhangList);
        model.addAttribute("title", "Kết quả tìm kiếm: " + keyword);
        return "khachhang/list";
    }
}