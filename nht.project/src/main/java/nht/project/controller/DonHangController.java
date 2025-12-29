package nht.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.dto.DonHangRequest;
import nht.project.dto.DonHangResponse;
import nht.project.dto.GioHangItem;
import nht.project.enums.PhuongThucThanhToan;
import nht.project.enums.TrangThaiDonHang;
import nht.project.model.DonHang;
import nht.project.model.KhachHang;
import nht.project.service.DonHangService;
import nht.project.service.GioHangService;
import nht.project.service.KhachHangService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/donhang")
@RequiredArgsConstructor
@Slf4j
public class DonHangController {

    private final DonHangService donHangService;
    private final GioHangService gioHangService;
    private final KhachHangService khachHangService;

    /**
     * Trang checkout (thanh toán)
     */
    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        log.info("Truy cập trang checkout");

        // Kiểm tra giỏ hàng
        if (gioHangService.isEmpty(session)) {
            return "redirect:/giohang";
        }

        // Cập nhật thông tin giỏ hàng
        gioHangService.capNhatThongTinGioHang(session);

        // Kiểm tra tồn kho
        if (!gioHangService.kiemTraTonKho(session)) {
            model.addAttribute("error", "Một số sản phẩm không đủ hàng. Vui lòng kiểm tra lại giỏ hàng.");
            return "redirect:/giohang";
        }

        List<GioHangItem> cart = gioHangService.getGioHang(session);
        BigDecimal tongTien = gioHangService.tinhTongTien(session);

        // Lấy thông tin khách hàng nếu đã đăng nhập
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

        model.addAttribute("cart", cart);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("phuongThucThanhToan", PhuongThucThanhToan.values());
        model.addAttribute("cartCount", gioHangService.demSoLuong(session));

        return "donhang/checkout";
    }

    /**
     * Xử lý đặt hàng
     */
    @PostMapping("/create")
    public String createOrder(@ModelAttribute DonHangRequest request,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        log.info("Tạo đơn hàng mới");

        try {
            // Kiểm tra giỏ hàng
            if (gioHangService.isEmpty(session)) {
                throw new IllegalStateException("Giỏ hàng trống");
            }

            // Tạo đơn hàng
            DonHang donHang = donHangService.taoDonHang(session, request);

            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
            redirectAttributes.addFlashAttribute("maDonHang", donHang.getMaDonHang());

            return "redirect:/donhang/confirm/" + donHang.getId();

        } catch (Exception e) {
            log.error("Lỗi khi tạo đơn hàng: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/donhang/checkout";
        }
    }

    /**
     * Trang xác nhận đơn hàng
     */
    @GetMapping("/confirm/{id}")
    public String confirmOrder(@PathVariable Long id, Model model, HttpSession session) {
        log.info("Xem xác nhận đơn hàng ID: {}", id);

        try {
            DonHang donHang = donHangService.getDonHangById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

            DonHangResponse response = donHangService.convertToResponse(donHang);

            model.addAttribute("donHang", response);
            model.addAttribute("cartCount", gioHangService.demSoLuong(session));

            return "donhang/confirm";

        } catch (Exception e) {
            log.error("Lỗi khi xem đơn hàng: {}", e.getMessage(), e);
            return "redirect:/";
        }
    }

    /**
     * Danh sách đơn hàng (của khách hàng)
     */
    @GetMapping("/list")
    public String listOrders(Model model, HttpSession session) {
        log.info("Xem danh sách đơn hàng");

        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

        if (khachHang == null) {
            return "redirect:/khachhang/login";
        }

        List<DonHang> donHangs = donHangService.getDonHangByKhachHang(khachHang.getId());

        model.addAttribute("donHangs", donHangs);
        model.addAttribute("cartCount", gioHangService.demSoLuong(session));

        return "donhang/list";
    }

    /**
     * Chi tiết đơn hàng
     * ✅ ĐÃ SỬA: Logic kiểm tra quyền an toàn hơn
     */
    @GetMapping("/{id}")
    public String detailOrder(@PathVariable Long id, Model model, HttpSession session) {
        log.info("Xem chi tiết đơn hàng ID: {}", id);

        try {
            DonHang donHang = donHangService.getDonHangById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

            // ✅ SỬA: Kiểm tra quyền xem AN TOÀN HƠN
            KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

            // Nếu là admin, cho xem tất cả
            boolean isAdmin = khachHang != null && "ADMIN".equals(khachHang.getRole());

            // Nếu không phải admin, kiểm tra có phải chủ đơn hàng không
            if (!isAdmin) {
                // Nếu đơn hàng có khách hàng
                if (donHang.getKhachHang() != null) {
                    // Phải đăng nhập và phải là chủ đơn hàng
                    if (khachHang == null || !donHang.getKhachHang().getId().equals(khachHang.getId())) {
                        log.warn("Người dùng {} cố truy cập đơn hàng {} không thuộc về họ",
                                khachHang != null ? khachHang.getId() : "guest", id);
                        return "redirect:/";
                    }
                }
                // Nếu đơn hàng không có khách hàng (khách vãng lai) thì ai cũng xem được
            }

            DonHangResponse response = donHangService.convertToResponse(donHang);

            model.addAttribute("donHang", response);
            model.addAttribute("cartCount", gioHangService.demSoLuong(session));

            return "donhang/detail";

        } catch (IllegalArgumentException e) {
            log.error("Không tìm thấy đơn hàng: {}", e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            log.error("Lỗi khi xem chi tiết đơn hàng: {}", e.getMessage(), e);
            return "redirect:/";
        }
    }

    /**
     * Tra cứu đơn hàng theo mã
     */
    @GetMapping("/track")
    public String trackOrder(@RequestParam(required = false) String maDonHang,
                             Model model,
                             HttpSession session) {
        log.info("Tra cứu đơn hàng: {}", maDonHang);

        if (maDonHang != null && !maDonHang.trim().isEmpty()) {
            DonHang donHang = donHangService.getDonHangByMa(maDonHang).orElse(null);

            if (donHang != null) {
                DonHangResponse response = donHangService.convertToResponse(donHang);
                model.addAttribute("donHang", response);
            } else {
                model.addAttribute("error", "Không tìm thấy đơn hàng với mã: " + maDonHang);
            }

            model.addAttribute("maDonHang", maDonHang);
        }

        model.addAttribute("cartCount", gioHangService.demSoLuong(session));

        return "donhang/track";
    }

    /**
     * Hủy đơn hàng
     */
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        log.info("Hủy đơn hàng ID: {}", id);

        try {
            DonHang donHang = donHangService.getDonHangById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

            // Kiểm tra quyền hủy
            KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

            // Kiểm tra an toàn
            if (donHang.getKhachHang() != null) {
                if (khachHang == null || !donHang.getKhachHang().getId().equals(khachHang.getId())) {
                    throw new IllegalStateException("Không có quyền hủy đơn hàng này");
                }
            }

            donHangService.huyDonHang(id);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công");

        } catch (Exception e) {
            log.error("Lỗi khi hủy đơn hàng: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/donhang/" + id;
    }
}