package K23cnt3.nht._2.controller.admin;

import K23cnt3.nht._2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private KhachhangService khachhangService;

    @Autowired
    private HoadonService hoadonService;

    @Autowired
    private NhanvienService nhanvienService;

    @Autowired
    private NhacungcapService nhacungcapService;

    @GetMapping("")
    public String dashboard(Model model) {
        // Thống kê cơ bản
        long tongSanPham = sanphamService.getAllSanpham().size();
        long tongKhachHang = khachhangService.getAllKhachhang().size();
        long tongDonHang = hoadonService.getAllHoadon().size();
        double doanhThu = hoadonService.tinhTongDoanhThu();

        // Đơn hàng hôm nay
        LocalDate homNay = LocalDate.now();
        long donHangHomNay = hoadonService.getHoadonByNgay(homNay).size();

        // Đơn hàng chờ xử lý
        long donHangChoXuLy = hoadonService.getHoadonByTrangThai("Chờ xử lý").size();

        model.addAttribute("tongSanPham", tongSanPham);
        model.addAttribute("tongKhachHang", tongKhachHang);
        model.addAttribute("tongDonHang", tongDonHang);
        model.addAttribute("doanhThu", doanhThu);
        model.addAttribute("donHangHomNay", donHangHomNay);
        model.addAttribute("donHangChoXuLy", donHangChoXuLy);

        return "admin/dashboard";
    }
}