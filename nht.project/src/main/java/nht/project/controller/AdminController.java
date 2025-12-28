package nht.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.dto.ThongKeResponse;
import nht.project.enums.PhuongThucThanhToan;
import nht.project.enums.TrangThaiDonHang;
import nht.project.enums.TrangThaiSanPham;
import nht.project.model.DanhMuc;
import nht.project.model.DonHang;
import nht.project.model.KhachHang;
import nht.project.model.SanPham;
import nht.project.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final SanPhamService sanPhamService;
    private final DonHangService donHangService;
    private final KhachHangService khachHangService;
    private final DanhMucService danhMucService;
    private final ThanhToanService thanhToanService;

    /**
     * Kiểm tra quyền admin
     */
    private boolean checkAdmin(HttpSession session) {
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");
        return khachHang != null && "ADMIN".equals(khachHang.getRole());
    }

    /**
     * Trang chủ admin - Dashboard
     */
    @GetMapping
    public String dashboard(Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        log.info("Admin truy cập dashboard");

        // Thống kê
        ThongKeResponse thongKe = new ThongKeResponse();

        // Sản phẩm
        thongKe.setTongSoSanPham((long) sanPhamService.getAllSanPham().size());
        thongKe.setSanPhamConHang(sanPhamService.countAvailableProducts());
        thongKe.setSanPhamHetHang(sanPhamService.countByStatus(TrangThaiSanPham.OUT_OF_STOCK));
        thongKe.setGiaTriTonKho(sanPhamService.calculateTotalInventoryValue());

        // Đơn hàng
        thongKe.setTongSoDonHang((long) donHangService.getAllDonHang().size());
        thongKe.setDonHangChoXuLy(donHangService.countByTrangThai(TrangThaiDonHang.CHO_XU_LY));
        thongKe.setDonHangDangGiao(donHangService.countByTrangThai(TrangThaiDonHang.DANG_GIAO));
        thongKe.setDonHangHoanThanh(donHangService.countByTrangThai(TrangThaiDonHang.HOAN_THANH));
        thongKe.setDonHangDaHuy(donHangService.countByTrangThai(TrangThaiDonHang.HUY));

        // Doanh thu
        thongKe.setTongDoanhThu(donHangService.tinhTongDoanhThu());
        thongKe.setDoanhThuTienMat(thanhToanService.tinhDoanhThuTheoPhuongThuc(PhuongThucThanhToan.TIEN_MAT));
        thongKe.setDoanhThuChuyenKhoan(thanhToanService.tinhDoanhThuTheoPhuongThuc(PhuongThucThanhToan.CHUYEN_KHOAN));

        // Khách hàng
        thongKe.setTongSoKhachHang((long) khachHangService.getAllKhachHang().size());

        model.addAttribute("thongKe", thongKe);
        model.addAttribute("isAdmin", true);

        // Đơn hàng gần đây
        List<DonHang> donHangGanDay = donHangService.getAllDonHang();
        if (donHangGanDay.size() > 10) {
            donHangGanDay = donHangGanDay.subList(0, 10);
        }
        model.addAttribute("donHangGanDay", donHangGanDay);

        // Sản phẩm sắp hết hàng
        List<SanPham> sanPhamSapHet = sanPhamService.getLowStockProducts(10);
        model.addAttribute("sanPhamSapHet", sanPhamSapHet);

        return "admin/dashboard";
    }

    // ============================================
    // QUẢN LÝ SẢN PHẨM
    // ============================================

    /**
     * Danh sách sản phẩm
     */
    @GetMapping("/sanpham")
    public String quanLySanPham(Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        log.info("Admin xem danh sách sản phẩm");

        List<SanPham> sanPhams = sanPhamService.getAllSanPham();
        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("danhMucs", danhMucService.getAllDanhMuc());
        model.addAttribute("isAdmin", true);

        return "admin/sanpham/list";
    }

    /**
     * Form thêm sản phẩm
     */
    @GetMapping("/sanpham/create")
    public String formThemSanPham(Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        model.addAttribute("danhMucs", danhMucService.getAllDanhMuc());
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("isAdmin", true);

        return "admin/sanpham/form";
    }

    /**
     * Xử lý thêm sản phẩm
     */
    @PostMapping("/sanpham/create")
    public String themSanPham(@ModelAttribute SanPham sanPham,
                              @RequestParam Long danhMucId,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            DanhMuc danhMuc = danhMucService.getDanhMucById(danhMucId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

            sanPham.setDanhMuc(danhMuc);
            sanPhamService.createSanPham(sanPham);

            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            log.error("Lỗi thêm sản phẩm: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/sanpham";
    }

    /**
     * Form sửa sản phẩm
     */
    @GetMapping("/sanpham/{id}/edit")
    public String formSuaSanPham(@PathVariable Long id, Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        SanPham sanPham = sanPhamService.getSanPhamById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        model.addAttribute("sanPham", sanPham);
        model.addAttribute("danhMucs", danhMucService.getAllDanhMuc());
        model.addAttribute("isAdmin", true);

        return "admin/sanpham/form";
    }

    /**
     * Xử lý sửa sản phẩm
     */
    @PostMapping("/sanpham/{id}/edit")
    public String suaSanPham(@PathVariable Long id,
                             @ModelAttribute SanPham sanPham,
                             @RequestParam Long danhMucId,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            DanhMuc danhMuc = danhMucService.getDanhMucById(danhMucId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

            sanPham.setId(id);
            sanPham.setDanhMuc(danhMuc);
            sanPhamService.updateSanPham(id, sanPham);

            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            log.error("Lỗi cập nhật sản phẩm: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/sanpham";
    }

    /**
     * Xóa sản phẩm
     */
    @PostMapping("/sanpham/{id}/delete")
    public String xoaSanPham(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            sanPhamService.deleteSanPham(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            log.error("Lỗi xóa sản phẩm: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/sanpham";
    }

    // ============================================
    // QUẢN LÝ ĐỚN HÀNG
    // ============================================

    /**
     * Danh sách đơn hàng
     */
    @GetMapping("/donhang")
    public String quanLyDonHang(Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        log.info("Admin xem danh sách đơn hàng");

        List<DonHang> donHangs = donHangService.getAllDonHang();
        model.addAttribute("donHangs", donHangs);
        model.addAttribute("isAdmin", true);

        return "admin/donhang/list";
    }

    /**
     * Chi tiết đơn hàng
     */
    @GetMapping("/donhang/{id}")
    public String chiTietDonHang(@PathVariable Long id, Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        DonHang donHang = donHangService.getDonHangById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        model.addAttribute("donHang", donHang);
        model.addAttribute("isAdmin", true);

        return "admin/donhang/detail";
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @PostMapping("/donhang/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam String trangThai,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            TrangThaiDonHang trangThaiMoi = TrangThaiDonHang.valueOf(trangThai);
            donHangService.capNhatTrangThai(id, trangThaiMoi);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            log.error("Lỗi cập nhật trạng thái: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/donhang";
    }

    /**
     * Xóa đơn hàng
     */
    @PostMapping("/donhang/{id}/delete")
    public String xoaDonHang(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            donHangService.huyDonHang(id);
            redirectAttributes.addFlashAttribute("success", "Xóa đơn hàng thành công!");
        } catch (Exception e) {
            log.error("Lỗi xóa đơn hàng: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/donhang";
    }

    // ============================================
    // QUẢN LÝ KHÁCH HÀNG
    // ============================================

    /**
     * Danh sách khách hàng
     */
    @GetMapping("/khachhang")
    public String quanLyKhachHang(Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        log.info("Admin xem danh sách khách hàng");

        List<KhachHang> khachHangs = khachHangService.getAllKhachHang();
        model.addAttribute("khachHangs", khachHangs);
        model.addAttribute("isAdmin", true);

        return "admin/khachhang/list";
    }

    /**
     * Chi tiết khách hàng
     */
    @GetMapping("/khachhang/{id}")
    public String chiTietKhachHang(@PathVariable Long id, Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        KhachHang khachHang = khachHangService.getKhachHangById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng"));

        // Lấy đơn hàng của khách hàng
        List<DonHang> donHangs = donHangService.getDonHangByKhachHang(id);

        model.addAttribute("khachHang", khachHang);
        model.addAttribute("donHangs", donHangs);
        model.addAttribute("isAdmin", true);

        return "admin/khachhang/detail";
    }

    /**
     * Xóa khách hàng
     */
    @PostMapping("/khachhang/{id}/delete")
    public String xoaKhachHang(@PathVariable Long id,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            khachHangService.deleteKhachHang(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
        } catch (Exception e) {
            log.error("Lỗi xóa khách hàng: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/khachhang";
    }

    // ============================================
    // QUẢN LÝ DANH MỤC
    // ============================================

    /**
     * Danh sách danh mục
     */
    @GetMapping("/danhmuc")
    public String quanLyDanhMuc(Model model, HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        log.info("Admin xem danh sách danh mục");

        List<DanhMuc> danhMucs = danhMucService.getAllDanhMuc();
        model.addAttribute("danhMucs", danhMucs);
        model.addAttribute("isAdmin", true);

        return "admin/danhmuc/list";
    }

    /**
     * Thêm danh mục mới
     */
    @PostMapping("/danhmuc/create")
    public String themDanhMuc(@RequestParam String tenDanhMuc,
                              @RequestParam(required = false) String moTa,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            DanhMuc danhMuc = new DanhMuc();
            danhMuc.setTenDanhMuc(tenDanhMuc);
            danhMuc.setMoTa(moTa);

            danhMucService.createDanhMuc(danhMuc);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
        } catch (Exception e) {
            log.error("Lỗi thêm danh mục: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/danhmuc";
    }

    /**
     * Cập nhật danh mục
     */
    @PostMapping("/danhmuc/{id}/update")
    public String capNhatDanhMuc(@PathVariable Long id,
                                 @RequestParam String tenDanhMuc,
                                 @RequestParam(required = false) String moTa,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            DanhMuc danhMuc = new DanhMuc();
            danhMuc.setTenDanhMuc(tenDanhMuc);
            danhMuc.setMoTa(moTa);

            danhMucService.updateDanhMuc(id, danhMuc);
            redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");
        } catch (Exception e) {
            log.error("Lỗi cập nhật danh mục: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/danhmuc";
    }

    /**
     * Xóa danh mục
     */
    @PostMapping("/danhmuc/{id}/delete")
    public String xoaDanhMuc(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        if (!checkAdmin(session)) {
            return "redirect:/khachhang/login";
        }

        try {
            danhMucService.deleteDanhMuc(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        } catch (Exception e) {
            log.error("Lỗi xóa danh mục: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục còn sản phẩm!");
        }

        return "redirect:/admin/danhmuc";
    }

    /**
     * API lấy thông tin danh mục (cho AJAX)
     */
    @GetMapping("/danhmuc/{id}/json")
    @ResponseBody
    public DanhMuc getDanhMucJson(@PathVariable Long id, HttpSession session) {
        if (!checkAdmin(session)) {
            return null;
        }

        return danhMucService.getDanhMucById(id).orElse(null);
    }
}