package nht.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.model.DanhMuc;
import nht.project.model.SanPham;
import nht.project.service.DanhMucService;
import nht.project.service.GioHangService;
import nht.project.service.SanPhamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final SanPhamService sanPhamService;
    private final DanhMucService danhMucService;
    private final GioHangService gioHangService;

    /**
     * Trang chủ
     */
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        log.info("Truy cập trang chủ");

        // Lấy danh mục có sản phẩm
        List<DanhMuc> danhMucs = danhMucService.getDanhMucWithAvailableProducts();
        model.addAttribute("danhMucs", danhMucs);

        // Lấy sản phẩm mới nhất
        List<SanPham> sanPhamMoi = sanPhamService.getNewProducts(8);
        model.addAttribute("sanPhamMoi", sanPhamMoi);

        // Lấy sản phẩm bán chạy
        List<SanPham> sanPhamBanChay = sanPhamService.getBestSellingProducts(8);
        model.addAttribute("sanPhamBanChay", sanPhamBanChay);

        // Số lượng giỏ hàng
        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);

        return "home";
    }

    /**
     * Trang giới thiệu
     */
    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);
        return "about";
    }

    /**
     * Trang liên hệ
     */
    @GetMapping("/contact")
    public String contact(Model model, HttpSession session) {
        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);
        return "contact";
    }

    /**
     * Tìm kiếm sản phẩm
     */
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         Model model,
                         HttpSession session) {
        log.info("Tìm kiếm với từ khóa: {}", keyword);

        List<DanhMuc> danhMucs = danhMucService.getAllDanhMuc();
        model.addAttribute("danhMucs", danhMucs);

        List<SanPham> sanPhams = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            sanPhams = sanPhamService.searchSanPham(keyword);
            model.addAttribute("keyword", keyword);
            model.addAttribute("resultCount", sanPhams.size());
        } else {
            sanPhams = sanPhamService.getAvailableSanPham();
        }

        model.addAttribute("sanPhams", sanPhams);
        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);

        return "sanpham/list";
    }
}