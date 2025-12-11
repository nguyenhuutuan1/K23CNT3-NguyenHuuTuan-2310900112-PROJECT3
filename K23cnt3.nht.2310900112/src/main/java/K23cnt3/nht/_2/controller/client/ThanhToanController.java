package K23cnt3.nht._2.controller.client;

import K23cnt3.nht._2.entity.*;
import K23cnt3.nht._2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/thanh-toan")
public class ThanhToanController {

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private HoadonService hoadonService;

    @Autowired
    private ChitiethoadonService chitiethoadonService;

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private KhachhangService khachhangService;

    @Autowired
    private NhanvienService nhanvienService;

    @GetMapping
    public String thanhToanPage(Model model) {
        Map<Integer, Integer> gioHang = gioHangService.getGioHang();

        if (gioHang.isEmpty()) {
            return "redirect:/gio-hang";
        }

        Map<Sanpham, Integer> chiTietGioHang = new HashMap<>();
        BigDecimal tongTien = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : gioHang.entrySet()) {
            Sanpham sanpham = gioHangService.getSanPhamById(entry.getKey());
            if (sanpham != null) {
                chiTietGioHang.put(sanpham, entry.getValue());
                if (sanpham.getDonGia() != null) {
                    tongTien = tongTien.add(sanpham.getDonGia().multiply(BigDecimal.valueOf(entry.getValue())));
                }
            }
        }

        model.addAttribute("gioHang", chiTietGioHang);
        model.addAttribute("tongTien", tongTien);

        return "client/thanhtoan/index";
    }

    @PostMapping("/dat-hang")
    public String datHang(@RequestParam String hoTen,
                          @RequestParam String diaChi,
                          @RequestParam String dienThoai,
                          @RequestParam String email,
                          @RequestParam String ghiChu,
                          RedirectAttributes redirectAttributes) {

        // Kiểm tra giỏ hàng
        Map<Integer, Integer> gioHang = gioHangService.getGioHang();
        if (gioHang.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/gio-hang";
        }

        // Tạo khách hàng mới hoặc lấy khách hàng hiện có
        Khachhang khachhang = khachhangService.getKhachhangByEmail(email);
        if (khachhang == null) {
            khachhang = new Khachhang();
            khachhang.setHoTen(hoTen);
            khachhang.setDiaChi(diaChi);
            khachhang.setDienThoai(dienThoai);
            khachhang.setEmail(email);
            khachhang = khachhangService.saveKhachhang(khachhang);
        }

        // Lấy nhân viên đầu tiên (tạm thời)
        Nhanvien nhanvien = nhanvienService.getAllNhanvien().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Không có nhân viên"));

        // Tạo hóa đơn
        Hoadon hoadon = new Hoadon();
        hoadon.setKhachhang(khachhang);
        hoadon.setNhanvien(nhanvien);
        hoadon.setNgayLapHD(LocalDate.now());
        hoadon.setTrangThai("Chờ xử lý");
        hoadon.setTongTien(BigDecimal.ZERO);

        hoadon = hoadonService.saveHoadon(hoadon);

        // Tạo chi tiết hóa đơn và cập nhật tồn kho
        BigDecimal tongTien = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : gioHang.entrySet()) {
            Sanpham sanpham = sanphamService.getSanphamById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Kiểm tra tồn kho
            if (sanpham.getSoLuong() < entry.getValue()) {
                throw new RuntimeException("Sản phẩm " + sanpham.getTenSP() + " không đủ số lượng");
            }

            // Tạo chi tiết hóa đơn
            Chitiethoadon chitiethoadon = new Chitiethoadon();
            chitiethoadon.setHoadon(hoadon);
            chitiethoadon.setSanpham(sanpham);
            chitiethoadon.setSoLuong(entry.getValue());
            chitiethoadon.setDonGia(sanpham.getDonGia());
            chitiethoadon.setThanhTien(sanpham.getDonGia().multiply(BigDecimal.valueOf(entry.getValue())));

            chitiethoadonService.saveChitiethoadon(chitiethoadon);

            // Cộng dồn tổng tiền
            tongTien = tongTien.add(chitiethoadon.getThanhTien());

            // Cập nhật tồn kho
            sanpham.setSoLuong(sanpham.getSoLuong() - entry.getValue());
            sanphamService.saveSanpham(sanpham);
        }

        // Cập nhật tổng tiền hóa đơn
        hoadon.setTongTien(tongTien);
        hoadonService.saveHoadon(hoadon);

        // Xóa giỏ hàng
        gioHangService.xoaToanBoGioHang();

        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công! Mã đơn hàng: " + hoadon.getMaHD());
        return "redirect:/";
    }
}