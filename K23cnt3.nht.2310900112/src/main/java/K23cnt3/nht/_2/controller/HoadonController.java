package K23cnt3.nht._2.controller;

import K23cnt3.nht._2.entity.Chitiethoadon;
import K23cnt3.nht._2.entity.Hoadon;
import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.service.HoadonService;
import K23cnt3.nht._2.service.KhachhangService;
import K23cnt3.nht._2.service.NhanvienService;
import K23cnt3.nht._2.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hoadon")
public class HoadonController {

    @Autowired
    private HoadonService hoadonService;

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private KhachhangService khachhangService;

    @Autowired
    private NhanvienService nhanvienService;

    @GetMapping
    public String getAllHoadon(Model model) {
        List<Hoadon> hoadonList = hoadonService.getAllHoadon();
        model.addAttribute("hoadonList", hoadonList);
        model.addAttribute("title", "Danh sách hóa đơn");
        return "hoadon/list";
    }

    @GetMapping("/{id}")
    public String getHoadonById(@PathVariable Integer id, Model model) {
        Hoadon hoadon = hoadonService.getHoadonById(id);
        model.addAttribute("hoadon", hoadon);
        model.addAttribute("title", "Chi tiết hóa đơn #" + id);
        return "hoadon/detail";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Hoadon hoadon = new Hoadon();
        hoadon.setNgayLapHD(LocalDate.now());

        model.addAttribute("hoadon", hoadon);
        model.addAttribute("sanphamList", sanphamService.getSanphamConHang());
        model.addAttribute("khachhangList", khachhangService.getAllKhachhang());
        model.addAttribute("nhanvienList", nhanvienService.getAllNhanvien());
        model.addAttribute("title", "Tạo hóa đơn mới");
        return "hoadon/create";
    }

    @PostMapping("/save")
    public String saveHoadon(@ModelAttribute Hoadon hoadon,
                             @RequestParam(value = "sanphamIds", required = false) List<Integer> sanphamIds,
                             @RequestParam(value = "quantities", required = false) List<Integer> quantities,
                             RedirectAttributes redirectAttributes) {

        if (sanphamIds == null || quantities == null || sanphamIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất một sản phẩm!");
            return "redirect:/hoadon/create";
        }

        List<Chitiethoadon> chiTietList = new ArrayList<>();

        for (int i = 0; i < sanphamIds.size(); i++) {
            Integer sanphamId = sanphamIds.get(i);
            Integer quantity = quantities.get(i);

            if (quantity != null && quantity > 0) {
                Sanpham sanpham = new Sanpham();
                sanpham.setMaSP(sanphamId);

                Chitiethoadon chiTiet = new Chitiethoadon();
                chiTiet.setSanPham(sanpham);
                chiTiet.setSoLuong(quantity);

                chiTietList.add(chiTiet);
            }
        }

        try {
            hoadonService.createHoadon(hoadon, chiTietList);
            redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/hoadon/create";
        }

        return "redirect:/hoadon";
    }

    @GetMapping("/delete/{id}")
    public String deleteHoadon(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            hoadonService.deleteHoadon(id);
            redirectAttributes.addFlashAttribute("success", "Xóa hóa đơn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/hoadon";
    }

    @GetMapping("/trangthai/{trangThai}")
    public String getHoadonByTrangThai(@PathVariable String trangThai, Model model) {
        List<Hoadon> hoadonList = hoadonService.getHoadonByTrangThai(trangThai);
        model.addAttribute("hoadonList", hoadonList);
        model.addAttribute("title", "Hóa đơn " + trangThai);
        return "hoadon/list";
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Integer id,
                               @RequestParam String trangThai,
                               RedirectAttributes redirectAttributes) {
        Hoadon hoadon = hoadonService.getHoadonById(id);
        if (hoadon != null) {
            hoadon.setTrangThai(trangThai);
            hoadonService.saveHoadon(hoadon);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/hoadon/" + id;
    }
}