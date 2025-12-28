package nht.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.dto.ApiResponse;
import nht.project.dto.GioHangItem;
import nht.project.service.GioHangService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/giohang")
@RequiredArgsConstructor
@Slf4j
public class GioHangController {

    private final GioHangService gioHangService;

    /**
     * Xem giỏ hàng
     */
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        log.info("Xem giỏ hàng");

        List<GioHangItem> cart = gioHangService.getGioHang(session);
        BigDecimal tongTien = gioHangService.tinhTongTien(session);
        int soLuong = gioHangService.demSoLuong(session);
        int soMatHang = gioHangService.demSoMatHang(session);

        model.addAttribute("cart", cart);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("soLuong", soLuong);
        model.addAttribute("soMatHang", soMatHang);
        model.addAttribute("cartCount", soLuong);

        return "giohang/index";
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam Long sanPhamId,
                            @RequestParam(defaultValue = "1") Integer soLuong,
                            RedirectAttributes redirectAttributes,
                            HttpSession session) {
        log.info("Thêm sản phẩm ID: {} vào giỏ hàng", sanPhamId);

        try {
            gioHangService.themSanPham(session, sanPhamId, soLuong);
            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng");
        } catch (Exception e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/giohang";
    }

    /**
     * Thêm vào giỏ hàng (AJAX)
     */
    @PostMapping("/add-ajax")
    @ResponseBody
    public ApiResponse<Integer> addToCartAjax(@RequestParam Long sanPhamId,
                                              @RequestParam(defaultValue = "1") Integer soLuong,
                                              HttpSession session) {
        log.info("Thêm sản phẩm ID: {} vào giỏ hàng (AJAX)", sanPhamId);

        try {
            gioHangService.themSanPham(session, sanPhamId, soLuong);
            int cartCount = gioHangService.demSoLuong(session);
            return ApiResponse.success("Đã thêm vào giỏ hàng", cartCount);
        } catch (Exception e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Cập nhật số lượng
     */
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long sanPhamId,
                                 @RequestParam Integer soLuong,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        log.info("Cập nhật số lượng sản phẩm ID: {} thành {}", sanPhamId, soLuong);

        try {
            gioHangService.capNhatSoLuong(session, sanPhamId, soLuong);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật số lượng");
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật số lượng: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/giohang";
    }

    /**
     * Cập nhật số lượng (AJAX)
     */
    @PostMapping("/update-ajax")
    @ResponseBody
    public ApiResponse<BigDecimal> updateQuantityAjax(@RequestParam Long sanPhamId,
                                                      @RequestParam Integer soLuong,
                                                      HttpSession session) {
        log.info("Cập nhật số lượng sản phẩm ID: {} thành {} (AJAX)", sanPhamId, soLuong);

        try {
            gioHangService.capNhatSoLuong(session, sanPhamId, soLuong);
            BigDecimal tongTien = gioHangService.tinhTongTien(session);
            return ApiResponse.success("Đã cập nhật", tongTien);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật số lượng: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long sanPhamId,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        log.info("Xóa sản phẩm ID: {} khỏi giỏ hàng", sanPhamId);

        try {
            gioHangService.xoaSanPham(session, sanPhamId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng");
        } catch (Exception e) {
            log.error("Lỗi khi xóa sản phẩm: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/giohang";
    }

    /**
     * Xóa sản phẩm (AJAX)
     */
    @PostMapping("/remove-ajax")
    @ResponseBody
    public ApiResponse<Integer> removeFromCartAjax(@RequestParam Long sanPhamId,
                                                   HttpSession session) {
        log.info("Xóa sản phẩm ID: {} khỏi giỏ hàng (AJAX)", sanPhamId);

        try {
            gioHangService.xoaSanPham(session, sanPhamId);
            int cartCount = gioHangService.demSoLuong(session);
            return ApiResponse.success("Đã xóa khỏi giỏ hàng", cartCount);
        } catch (Exception e) {
            log.error("Lỗi khi xóa sản phẩm: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes, HttpSession session) {
        log.info("Xóa toàn bộ giỏ hàng");

        gioHangService.xoaGioHang(session);
        redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng");

        return "redirect:/giohang";
    }

    /**
     * Lấy số lượng giỏ hàng (AJAX)
     */
    @GetMapping("/count")
    @ResponseBody
    public ApiResponse<Integer> getCartCount(HttpSession session) {
        int count = gioHangService.demSoLuong(session);
        return ApiResponse.success(count);
    }
}