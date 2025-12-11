package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.entity.Hoadon;
import K23cnt3.nht._2.service.ChitiethoadonService;
import K23cnt3.nht._2.service.HoadonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/thong-ke")
public class ThongKeController {

    @Autowired
    private HoadonService hoadonService;

    @Autowired
    private ChitiethoadonService chitiethoadonService;

    @GetMapping
    public String thongKePage(Model model) {
        // Thống kê cơ bản
        LocalDate now = LocalDate.now();
        double doanhThuThang = tinhDoanhThuThang(now.getMonthValue(), now.getYear());
        long donHangThang = demDonHangThang(now.getMonthValue(), now.getYear());

        // Sản phẩm bán chạy
        List<Object[]> sanPhamBanChay = chitiethoadonService.getSanPhamBanChay();

        model.addAttribute("doanhThuThang", doanhThuThang);
        model.addAttribute("donHangThang", donHangThang);
        model.addAttribute("sanPhamBanChay", sanPhamBanChay);

        return "admin/thongke/index";
    }

    @PostMapping("/theo-thang")
    public String thongKeTheoThang(@RequestParam Integer thang,
                                   @RequestParam Integer nam,
                                   Model model) {
        double doanhThu = tinhDoanhThuThang(thang, nam);
        long soDonHang = demDonHangThang(thang, nam);

        model.addAttribute("doanhThu", doanhThu);
        model.addAttribute("soDonHang", soDonHang);
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);

        return "admin/thongke/ketqua";
    }

    private double tinhDoanhThuThang(int thang, int nam) {
        LocalDate startDate = LocalDate.of(nam, thang, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Hoadon> donHangThang = hoadonService.getHoadonBetweenDates(startDate, endDate);

        return donHangThang.stream()
                .filter(h -> h.getTrangThai() != null && "Đã thanh toán".equals(h.getTrangThai()))
                .mapToDouble(h -> {
                    if (h.getTongTien() != null) {
                        return h.getTongTien().doubleValue();
                    }
                    return 0.0;
                })
                .sum();
    }

    private long demDonHangThang(int thang, int nam) {
        LocalDate startDate = LocalDate.of(nam, thang, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return hoadonService.getHoadonBetweenDates(startDate, endDate).size();
    }
}