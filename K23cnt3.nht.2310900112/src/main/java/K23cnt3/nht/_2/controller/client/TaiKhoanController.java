package K23cnt3.nht._2.controller.client;

import K23cnt3.nht._2.entity.Hoadon;
import K23cnt3.nht._2.entity.Khachhang;
import K23cnt3.nht._2.service.HoadonService;
import K23cnt3.nht._2.service.KhachhangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/tai-khoan")
public class TaiKhoanController {

    @Autowired
    private KhachhangService khachhangService;

    @Autowired
    private HoadonService hoadonService;

    @GetMapping("/dang-nhap")
    public String dangNhapPage() {
        return "client/taikhoan/dangnhap";
    }

    @PostMapping("/dang-nhap")
    public String dangNhap(@RequestParam String email,
                           @RequestParam String matKhau,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        // Tạm thời: chỉ kiểm tra email tồn tại
        Khachhang khachhang = khachhangService.getKhachhangByEmail(email);

        if (khachhang == null) {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại!");
            return "redirect:/tai-khoan/dang-nhap";
        }

        // Lưu thông tin khách hàng vào session
        session.setAttribute("khachHang", khachhang);
        redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");

        return "redirect:/";
    }

    @GetMapping("/dang-ky")
    public String dangKyPage() {
        return "client/taikhoan/dangky";
    }

    @PostMapping("/dang-ky")
    public String dangKy(@RequestParam String hoTen,
                         @RequestParam String email,
                         @RequestParam String dienThoai,
                         @RequestParam String diaChi,
                         @RequestParam String matKhau,
                         RedirectAttributes redirectAttributes) {

        // Kiểm tra email đã tồn tại
        if (khachhangService.getKhachhangByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:/tai-khoan/dang-ky";
        }

        // Tạo khách hàng mới
        Khachhang khachhang = new Khachhang();
        khachhang.setHoTen(hoTen);
        khachhang.setEmail(email);
        khachhang.setDienThoai(dienThoai);
        khachhang.setDiaChi(diaChi);
        // Note: Trong database thực tế cần có trường matKhau

        khachhangService.saveKhachhang(khachhang);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/tai-khoan/dang-nhap";
    }

    @GetMapping("/dang-xuat")
    public String dangXuat(HttpSession session) {
        session.removeAttribute("khachHang");
        return "redirect:/";
    }

    @GetMapping("/don-hang")
    public String donHangCuaToi(HttpSession session, Model model) {
        Khachhang khachhang = (Khachhang) session.getAttribute("khachHang");

        if (khachhang == null) {
            return "redirect:/tai-khoan/dang-nhap";
        }

        List<Hoadon> donHangList = hoadonService.getHoadonByKhachhang(khachhang.getMaKH());
        model.addAttribute("donHangList", donHangList);

        return "client/taikhoan/donhang";
    }

    @GetMapping("/don-hang/{maHD}")
    public String chiTietDonHang(@PathVariable Integer maHD,
                                 HttpSession session,
                                 Model model) {

        Khachhang khachhang = (Khachhang) session.getAttribute("khachHang");

        if (khachhang == null) {
            return "redirect:/tai-khoan/dang-nhap";
        }

        Hoadon hoadon = hoadonService.getHoadonById(maHD)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // Kiểm tra đơn hàng có thuộc về khách hàng này không
        if (!hoadon.getKhachhang().getMaKH().equals(khachhang.getMaKH())) {
            throw new RuntimeException("Bạn không có quyền xem đơn hàng này");
        }

        model.addAttribute("hoadon", hoadon);

        return "client/taikhoan/chitietdonhang";
    }
}