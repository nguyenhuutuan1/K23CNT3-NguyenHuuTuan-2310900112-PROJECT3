package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.entity.Nhacungcap;
import K23cnt3.nht._2.service.NhacungcapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/nhacungcap")
public class NhacungcapController {

    @Autowired
    private NhacungcapService nhacungcapService;

    @GetMapping
    public String getAllNhacungcap(Model model) {
        List<Nhacungcap> nhacungcapList = nhacungcapService.getAllNhacungcap();
        model.addAttribute("nhacungcapList", nhacungcapList);
        model.addAttribute("title", "Danh sách nhà cung cấp");
        return "nhacungcap/list";
    }

    @GetMapping("/{id}")
    public String getNhacungcapById(@PathVariable Integer id, Model model) {
        Nhacungcap nhacungcap = nhacungcapService.getNhacungcapById(id);
        model.addAttribute("nhacungcap", nhacungcap);
        model.addAttribute("title", "Chi tiết nhà cung cấp: " + nhacungcap.getTenNCC());
        return "nhacungcap/detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("nhacungcap", new Nhacungcap());
        model.addAttribute("title", "Thêm nhà cung cấp mới");
        return "nhacungcap/form";
    }

    @PostMapping("/save")
    public String saveNhacungcap(@ModelAttribute Nhacungcap nhacungcap,
                                 RedirectAttributes redirectAttributes) {
        nhacungcapService.saveNhacungcap(nhacungcap);
        redirectAttributes.addFlashAttribute("success", "Lưu nhà cung cấp thành công!");
        return "redirect:/nhacungcap";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Nhacungcap nhacungcap = nhacungcapService.getNhacungcapById(id);
        model.addAttribute("nhacungcap", nhacungcap);
        model.addAttribute("title", "Chỉnh sửa nhà cung cấp: " + nhacungcap.getTenNCC());
        return "nhacungcap/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteNhacungcap(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        nhacungcapService.deleteNhacungcap(id);
        redirectAttributes.addFlashAttribute("success", "Xóa nhà cung cấp thành công!");
        return "redirect:/nhacungcap";
    }

    @GetMapping("/search")
    public String searchNhacungcap(@RequestParam String keyword, Model model) {
        List<Nhacungcap> nhacungcapList = nhacungcapService.searchNhacungcap(keyword);
        model.addAttribute("nhacungcapList", nhacungcapList);
        model.addAttribute("title", "Kết quả tìm kiếm: " + keyword);
        return "nhacungcap/list";
    }
}