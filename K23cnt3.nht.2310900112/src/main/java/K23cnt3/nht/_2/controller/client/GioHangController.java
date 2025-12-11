package K23cnt3.nht._2.controller.client;

import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.service.GioHangService;
import K23cnt3.nht._2.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private SanphamService sanphamService;

    @GetMapping
    public String viewGioHang(Model model) {
        Map<Integer, Integer> gioHang = gioHangService.getGioHang();
        Map<Sanpham, Integer> chiTietGioHang = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : gioHang.entrySet()) {
            Sanpham sanpham = gioHangService.getSanPhamById(entry.getKey());
            if (sanpham != null) {
                chiTietGioHang.put(sanpham, entry.getValue());
            }
        }

        model.addAttribute("gioHang", chiTietGioHang);
        model.addAttribute("tongTien", gioHangService.getTongTien());
        model.addAttribute("tongSoLuong", gioHangService.getTongSoLuong());

        return "client/giohang/index";
    }

    @PostMapping("/them")
    public String themVaoGioHang(@RequestParam Integer maSP,
                                 @RequestParam(defaultValue = "1") Integer soLuong,
                                 RedirectAttributes redirectAttributes) {

        if (!gioHangService.kiemTraSanPhamConHang(maSP, soLuong)) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không đủ số lượng!");
            return "redirect:/san-pham/" + maSP;
        }

        gioHangService.themSanPhamVaoGio(maSP, soLuong);
        redirectAttributes.addFlashAttribute("success", "Đã thêm vào giỏ hàng!");

        return "redirect:/san-pham/" + maSP;
    }

    @PostMapping("/cap-nhat")
    public String capNhatGioHang(@RequestParam Integer maSP,
                                 @RequestParam Integer soLuong) {

        gioHangService.capNhatSoLuong(maSP, soLuong);

        return "redirect:/gio-hang";
    }

    @PostMapping("/xoa")
    public String xoaKhoiGioHang(@RequestParam Integer maSP) {
        gioHangService.xoaSanPhamKhoiGio(maSP);

        return "redirect:/gio-hang";
    }

    @PostMapping("/xoa-tat-ca")
    public String xoaTatCaGioHang() {
        gioHangService.xoaToanBoGioHang();

        return "redirect:/gio-hang";
    }
}