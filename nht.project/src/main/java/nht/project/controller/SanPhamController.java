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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/sanpham")
@RequiredArgsConstructor
@Slf4j
public class SanPhamController {

    private final SanPhamService sanPhamService;
    private final DanhMucService danhMucService;
    private final GioHangService gioHangService;

    /**
     * Danh sách tất cả sản phẩm
     */
    @GetMapping
    public String listAll(Model model, HttpSession session) {
        log.info("Xem danh sách tất cả sản phẩm");

        List<DanhMuc> danhMucs = danhMucService.getAllDanhMuc();
        List<SanPham> sanPhams = sanPhamService.getAvailableSanPham();

        model.addAttribute("danhMucs", danhMucs);
        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("title", "Tất cả sản phẩm");

        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);

        return "sanpham/list";
    }

    /**
     * Danh sách sản phẩm theo danh mục
     */
    @GetMapping("/danhmuc/{danhMucId}")
    public String listByDanhMuc(@PathVariable Long danhMucId,
                                Model model,
                                HttpSession session) {
        log.info("Xem sản phẩm theo danh mục ID: {}", danhMucId);

        DanhMuc danhMuc = danhMucService.getDanhMucById(danhMucId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

        List<DanhMuc> danhMucs = danhMucService.getAllDanhMuc();
        List<SanPham> sanPhams = sanPhamService.getAvailableSanPhamByDanhMuc(danhMucId);

        model.addAttribute("danhMucs", danhMucs);
        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("selectedDanhMuc", danhMuc);
        model.addAttribute("title", danhMuc.getTenDanhMuc());

        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);

        return "sanpham/list";
    }

    /**
     * Chi tiết sản phẩm
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        log.info("Xem chi tiết sản phẩm ID: {}", id);

        SanPham sanPham = sanPhamService.getSanPhamById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        // Lấy sản phẩm liên quan (cùng danh mục)
        List<SanPham> sanPhamLienQuan = sanPhamService.getAvailableSanPhamByDanhMuc(
                sanPham.getDanhMuc().getId()
        );
        sanPhamLienQuan.removeIf(sp -> sp.getId().equals(id)); // Loại bỏ sản phẩm hiện tại
        if (sanPhamLienQuan.size() > 4) {
            sanPhamLienQuan = sanPhamLienQuan.subList(0, 4);
        }

        model.addAttribute("sanPham", sanPham);
        model.addAttribute("sanPhamLienQuan", sanPhamLienQuan);

        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);

        return "sanpham/detail";
    }

    /**
     * Lọc sản phẩm theo giá
     */
    @GetMapping("/filter")
    public String filter(@RequestParam(required = false) BigDecimal minPrice,
                         @RequestParam(required = false) BigDecimal maxPrice,
                         @RequestParam(required = false) Long danhMucId,
                         Model model,
                         HttpSession session) {
        log.info("Lọc sản phẩm - Min: {}, Max: {}, DanhMuc: {}", minPrice, maxPrice, danhMucId);

        List<DanhMuc> danhMucs = danhMucService.getAllDanhMuc();
        model.addAttribute("danhMucs", danhMucs);

        List<SanPham> sanPhams;

        if (danhMucId != null) {
            sanPhams = sanPhamService.getAvailableSanPhamByDanhMuc(danhMucId);
            DanhMuc selectedDanhMuc = danhMucService.getDanhMucById(danhMucId).orElse(null);
            model.addAttribute("selectedDanhMuc", selectedDanhMuc);
        } else {
            sanPhams = sanPhamService.getAvailableSanPham();
        }

        // Lọc theo giá
        if (minPrice != null && maxPrice != null) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> sp.getGia().compareTo(minPrice) >= 0
                            && sp.getGia().compareTo(maxPrice) <= 0)
                    .toList();
        }

        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("title", "Kết quả lọc");

        int cartCount = gioHangService.demSoLuong(session);
        model.addAttribute("cartCount", cartCount);

        return "sanpham/list";
    }
}