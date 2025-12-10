package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.entity.Nhanvien;
import K23cnt3.nht._2.service.NhanvienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/nhanvien")
public class NhanvienController {

    @Autowired
    private NhanvienService nhanvienService;

    @GetMapping
    public String getAllNhanvien(Model model) {
        List<Nhanvien> nhanvienList = nhanvienService.getAllNhanvien();
        model.addAttribute("nhanvienList", nhanvienList);
        model.addAttribute("title", "Danh sách nhân viên");
        return "nhanvien/list";
    }

    @GetMapping("/{id}")
    public String getNhanvienById(@PathVariable Integer id, Model model) {
        Nhanvien nhanvien = nhanvienService.getNhanvienById(id);
        model.addAttribute("nhanvien", nhanvien);
        model.addAttribute("title", "Chi tiết nhân viên: " + nhanvien.getHoTen());
        return "nhanvien/detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("nhanvien", new Nhanvien());
        model.addAttribute("title", "Thêm nhân viên mới");
        return "nhanvien/form";
    }

    @PostMapping("/save")
    public String saveNhanvien(@ModelAttribute Nhanvien nhanvien,
                               RedirectAttributes redirectAttributes) {
        nhanvienService.saveNhanvien(nhanvien);
        redirectAttributes.addFlashAttribute("success", "Lưu nhân viên thành công!");
        return "redirect:/nhanvien";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Nhanvien nhanvien = nhanvienService.getNhanvienById(id);
        model.addAttribute("nhanvien", nhanvien);
        model.addAttribute("title", "Chỉnh sửa nhân viên: " + nhanvien.getHoTen());
        return "nhanvien/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteNhanvien(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        nhanvienService.deleteNhanvien(id);
        redirectAttributes.addFlashAttribute("success", "Xóa nhân viên thành công!");
        return "redirect:/nhanvien";
    }

    @GetMapping("/search")
    public String searchNhanvien(@RequestParam String keyword, Model model) {
        List<Nhanvien> nhanvienList = nhanvienService.searchNhanvien(keyword);
        model.addAttribute("nhanvienList", nhanvienList);
        model.addAttribute("title", "Kết quả tìm kiếm: " + keyword);
        return "nhanvien/list";
    }

    @GetMapping("/chucvu/{chucVu}")
    public String getNhanvienByChucVu(@PathVariable String chucVu, Model model) {
        List<Nhanvien> nhanvienList = nhanvienService.getNhanvienByChucVu(chucVu);
        model.addAttribute("nhanvienList", nhanvienList);
        model.addAttribute("title", "Nhân viên chức vụ: " + chucVu);
        return "nhanvien/list";
    }
}