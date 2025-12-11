package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.entity.Chitiethoadon;
import K23cnt3.nht._2.entity.Hoadon;
import K23cnt3.nht._2.service.ChitiethoadonService;
import K23cnt3.nht._2.service.HoadonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/don-hang")
public class DonHangAdminController {

    @Autowired
    private HoadonService hoadonService;

    @Autowired
    private ChitiethoadonService chitiethoadonService;

    @GetMapping
    public String danhSachDonHang(Model model) {
        List<Hoadon> donHangList = hoadonService.getAllHoadon();
        model.addAttribute("donHangList", donHangList);
        return "admin/donhang/list";
    }

    @GetMapping("/{id}")
    public String chiTietDonHang(@PathVariable Integer id, Model model) {
        Hoadon hoadon = hoadonService.getHoadonById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        List<Chitiethoadon> chiTietList = chitiethoadonService.getChitiethoadonByHoadon(id);

        model.addAttribute("hoadon", hoadon);
        model.addAttribute("chiTietList", chiTietList);

        return "admin/donhang/detail";
    }

    @PostMapping("/cap-nhat-trang-thai/{id}")
    public String capNhatTrangThai(@PathVariable Integer id,
                                   @RequestParam String trangThai,
                                   RedirectAttributes redirectAttributes) {
        Hoadon hoadon = hoadonService.getHoadonById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        hoadon.setTrangThai(trangThai);
        hoadonService.saveHoadon(hoadon);

        redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        return "redirect:/admin/don-hang/" + id;
    }

    @GetMapping("/xoa/{id}")
    public String xoaDonHang(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {
        hoadonService.deleteHoadon(id);
        redirectAttributes.addFlashAttribute("success", "Xóa đơn hàng thành công!");
        return "redirect:/admin/don-hang";
    }
}