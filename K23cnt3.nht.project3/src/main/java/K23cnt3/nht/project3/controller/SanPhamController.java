package K23cnt3.nht.project3.controller;

import K23cnt3.nht.project3.entity.Sanpham;
import K23cnt3.nht.project3.service.LoaisanphamService;
import K23cnt3.nht.project3.service.NhacungcapService;
import K23cnt3.nht.project3.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/sanpham")
public class SanphamController {

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private LoaisanphamService loaisanphamService;

    @Autowired
    private NhacungcapService nhacungcapService;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public String getAllSanpham(Model model) {
        List<Sanpham> sanphamList = sanphamService.getAllSanpham();
        model.addAttribute("sanphamList", sanphamList);
        model.addAttribute("title", "Danh sách sản phẩm");
        return "sanpham/list";
    }

    @GetMapping("/{id}")
    public String getSanphamById(@PathVariable Integer id, Model model) {
        Sanpham sanpham = sanphamService.getSanphamById(id);
        model.addAttribute("sanpham", sanpham);
        model.addAttribute("title", "Chi tiết sản phẩm: " + sanpham.getTenSP());
        return "sanpham/detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sanpham", new Sanpham());
        model.addAttribute("loaiList", loaisanphamService.getAllLoaisanpham());
        model.addAttribute("nccList", nhacungcapService.getAllNhacungcap());
        model.addAttribute("title", "Thêm sản phẩm mới");
        return "sanpham/form";
    }

    @PostMapping("/save")
    public String saveSanpham(@ModelAttribute Sanpham sanpham,
                              @RequestParam("imageFile") MultipartFile file,
                              RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {
                // Tạo thư mục uploads nếu chưa tồn tại
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                sanpham.setHinhAnh("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh!");
                return "redirect:/sanpham/add";
            }
        }

        sanphamService.saveSanpham(sanpham);
        redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công!");
        return "redirect:/sanpham";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Sanpham sanpham = sanphamService.getSanphamById(id);
        model.addAttribute("sanpham", sanpham);
        model.addAttribute("loaiList", loaisanphamService.getAllLoaisanpham());
        model.addAttribute("nccList", nhacungcapService.getAllNhacungcap());
        model.addAttribute("title", "Chỉnh sửa sản phẩm: " + sanpham.getTenSP());
        return "sanpham/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSanpham(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        sanphamService.deleteSanpham(id);
        redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        return "redirect:/sanpham";
    }

    @GetMapping("/search")
    public String searchSanpham(@RequestParam String keyword, Model model) {
        List<Sanpham> sanphamList = sanphamService.searchSanpham(keyword);
        model.addAttribute("sanphamList", sanphamList);
        model.addAttribute("title", "Kết quả tìm kiếm: " + keyword);
        return "sanpham/list";
    }

    @GetMapping("/conhang")
    public String getSanphamConHang(Model model) {
        List<Sanpham> sanphamList = sanphamService.getSanphamConHang();
        model.addAttribute("sanphamList", sanphamList);
        model.addAttribute("title", "Sản phẩm còn hàng");
        return "sanpham/list";
    }

    @GetMapping("/hethang")
    public String getSanphamHetHang(Model model) {
        List<Sanpham> sanphamList = sanphamService.getAllSanpham().stream()
                .filter(sp -> sp.getSoLuong() <= 0)
                .toList();
        model.addAttribute("sanphamList", sanphamList);
        model.addAttribute("title", "Sản phẩm hết hàng");
        return "sanpham/list";
    }
}