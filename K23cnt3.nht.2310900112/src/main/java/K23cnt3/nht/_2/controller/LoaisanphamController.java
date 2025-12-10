package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.entity.Loaisanpham;
import K23cnt3.nht._2.service.LoaisanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/loaisanpham")
public class LoaisanphamController {

    @Autowired
    private LoaisanphamService loaisanphamService;

    @GetMapping
    public String getAllLoaisanpham(Model model) {
        List<Loaisanpham> loaisanphamList = loaisanphamService.getAllLoaisanpham();
        model.addAttribute("loaisanphamList", loaisanphamList);
        model.addAttribute("title", "Danh sách loại sản phẩm");
        return "loaisanpham/list";
    }

    @GetMapping("/{id}")
    public String getLoaisanphamById(@PathVariable Integer id, Model model) {
        Loaisanpham loaisanpham = loaisanphamService.getLoaisanphamById(id);
        model.addAttribute("loaisanpham", loaisanpham);
        model.addAttribute("title", "Chi tiết loại sản phẩm: " + loaisanpham.getTenLoai());
        return "loaisanpham/detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("loaisanpham", new Loaisanpham());
        model.addAttribute("title", "Thêm loại sản phẩm mới");
        return "loaisanpham/form";
    }

    @PostMapping("/save")
    public String saveLoaisanpham(@ModelAttribute Loaisanpham loaisanpham,
                                  RedirectAttributes redirectAttributes) {
        loaisanphamService.saveLoaisanpham(loaisanpham);
        redirectAttributes.addFlashAttribute("success", "Lưu loại sản phẩm thành công!");
        return "redirect:/loaisanpham";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Loaisanpham loaisanpham = loaisanphamService.getLoaisanphamById(id);
        model.addAttribute("loaisanpham", loaisanpham);
        model.addAttribute("title", "Chỉnh sửa loại sản phẩm: " + loaisanpham.getTenLoai());
        return "loaisanpham/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteLoaisanpham(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        loaisanphamService.deleteLoaisanpham(id);
        redirectAttributes.addFlashAttribute("success", "Xóa loại sản phẩm thành công!");
        return "redirect:/loaisanpham";
    }

    @GetMapping("/search")
    public String searchLoaisanpham(@RequestParam String keyword, Model model) {
        List<Loaisanpham> loaisanphamList = loaisanphamService.searchLoaisanpham(keyword);
        model.addAttribute("loaisanphamList", loaisanphamList);
        model.addAttribute("title", "Kết quả tìm kiếm: " + keyword);
        return "loaisanpham/list";
    }
}