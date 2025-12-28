package nht.project.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.dto.DonHangRequest;
import nht.project.dto.DonHangResponse;
import nht.project.dto.GioHangItem;
import nht.project.enums.TrangThaiDonHang;
import nht.project.enums.TrangThaiThanhToan;
import nht.project.model.*;
import nht.project.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonHangService {

    private final DonHangRepository donHangRepository;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamRepository sanPhamRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final GioHangService gioHangService;

    /**
     * Lấy tất cả đơn hàng
     */
    public List<DonHang> getAllDonHang() {
        log.info("Lấy tất cả đơn hàng");
        return donHangRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayDat"));
    }

    /**
     * Lấy đơn hàng theo ID
     */
    public Optional<DonHang> getDonHangById(Long id) {
        log.info("Lấy đơn hàng ID: {}", id);
        return donHangRepository.findByIdWithDetails(id);
    }

    /**
     * Lấy đơn hàng theo mã
     */
    public Optional<DonHang> getDonHangByMa(String maDonHang) {
        log.info("Lấy đơn hàng mã: {}", maDonHang);
        return donHangRepository.findByMaDonHangWithDetails(maDonHang);
    }

    /**
     * Lấy đơn hàng của khách hàng
     */
    public List<DonHang> getDonHangByKhachHang(Long khachHangId) {
        log.info("Lấy đơn hàng của khách hàng ID: {}", khachHangId);
        return donHangRepository.findByKhachHangIdOrderByNgayDatDesc(khachHangId);
    }

    /**
     * Lấy đơn hàng theo trạng thái
     */
    public List<DonHang> getDonHangByTrangThai(TrangThaiDonHang trangThai) {
        log.info("Lấy đơn hàng trạng thái: {}", trangThai);
        return donHangRepository.findByTrangThaiDonHang(trangThai);
    }

    /**
     * Lấy đơn hàng chờ xử lý
     */
    public List<DonHang> getDonHangChoXuLy() {
        log.info("Lấy đơn hàng chờ xử lý");
        return donHangRepository.findPendingOrders();
    }

    /**
     * Lấy đơn hàng chưa thanh toán
     */
    public List<DonHang> getDonHangChuaThanhToan() {
        log.info("Lấy đơn hàng chưa thanh toán");
        return donHangRepository.findUnpaidOrders();
    }

    /**
     * Tạo đơn hàng từ giỏ hàng
     */
    @Transactional
    public DonHang taoDonHang(HttpSession session, DonHangRequest request) {
        log.info("Tạo đơn hàng mới cho khách hàng: {}", request.getHoTen());

        // Validate request
        if (!request.isValid()) {
            throw new IllegalArgumentException(request.getValidationMessage());
        }

        // Lấy giỏ hàng
        List<GioHangItem> cart = gioHangService.getGioHang(session);
        if (cart.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng trống");
        }

        // Kiểm tra tồn kho
        if (!gioHangService.kiemTraTonKho(session)) {
            throw new IllegalStateException("Một số sản phẩm không đủ hàng");
        }

        // Tạo hoặc lấy khách hàng
        KhachHang khachHang;
        if (request.getKhachHangId() != null) {
            khachHang = khachHangRepository.findById(request.getKhachHangId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng"));
        } else {
            // Tạo khách hàng mới (khách vãng lai)
            khachHang = new KhachHang();
            khachHang.setHoTen(request.getHoTen());
            khachHang.setSoDienThoai(request.getSoDienThoai());
            khachHang.setEmail(request.getEmail());
            khachHang.setDiaChi(request.getDiaChiGiaoHang());
            khachHang = khachHangRepository.save(khachHang);
        }

        // Tạo đơn hàng
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setPhuongThucThanhToan(request.getPhuongThucThanhToan());
        donHang.setDiaChiGiaoHang(request.getDiaChiGiaoHang());
        donHang.setGhiChu(request.getGhiChu());
        donHang.setTrangThaiDonHang(TrangThaiDonHang.CHO_XU_LY);
        donHang.setTrangThaiThanhToan(TrangThaiThanhToan.CHUA_THANH_TOAN);

        // Tạo chi tiết đơn hàng
        BigDecimal tongTien = BigDecimal.ZERO;
        for (GioHangItem item : cart) {
            SanPham sanPham = sanPhamRepository.findById(item.getSanPhamId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

            // Giảm số lượng tồn kho
            sanPham.giamSoLuong(item.getSoLuong());
            sanPhamRepository.save(sanPham);

            // Tạo chi tiết
            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setDonHang(donHang);
            chiTiet.setSanPham(sanPham);
            chiTiet.setSoLuong(item.getSoLuong());
            chiTiet.setGiaBan(item.getGia());
            chiTiet.setThanhTien(item.getThanhTien());

            donHang.themChiTiet(chiTiet);
            tongTien = tongTien.add(item.getThanhTien());
        }

        donHang.setTongTien(tongTien);

        // Lưu đơn hàng
        DonHang savedDonHang = donHangRepository.save(donHang);

        // Xóa giỏ hàng
        gioHangService.xoaGioHang(session);

        log.info("Đã tạo đơn hàng: {}", savedDonHang.getMaDonHang());
        return savedDonHang;
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @Transactional
    public DonHang capNhatTrangThai(Long donHangId, TrangThaiDonHang trangThaiMoi) {
        log.info("Cập nhật trạng thái đơn hàng ID: {} sang {}", donHangId, trangThaiMoi);

        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        TrangThaiDonHang trangThaiCu = donHang.getTrangThaiDonHang();

        // Validate chuyển trạng thái
        validateTrangThaiTransition(trangThaiCu, trangThaiMoi);

        donHang.setTrangThaiDonHang(trangThaiMoi);

        // Nếu hủy đơn, hoàn lại tồn kho
        if (trangThaiMoi == TrangThaiDonHang.HUY) {
            hoanLaiTonKho(donHang);
        }

        return donHangRepository.save(donHang);
    }

    /**
     * Hủy đơn hàng
     */
    @Transactional
    public void huyDonHang(Long donHangId) {
        log.info("Hủy đơn hàng ID: {}", donHangId);

        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        if (!donHang.coTheHuy()) {
            throw new IllegalStateException("Không thể hủy đơn hàng ở trạng thái này");
        }

        donHang.setTrangThaiDonHang(TrangThaiDonHang.HUY);

        // Hoàn lại tồn kho
        hoanLaiTonKho(donHang);

        donHangRepository.save(donHang);
    }

    /**
     * Hoàn lại tồn kho khi hủy đơn
     */
    private void hoanLaiTonKho(DonHang donHang) {
        log.info("Hoàn lại tồn kho cho đơn hàng: {}", donHang.getMaDonHang());

        for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
            SanPham sanPham = chiTiet.getSanPham();
            sanPham.tangSoLuong(chiTiet.getSoLuong());
            sanPhamRepository.save(sanPham);
        }
    }

    /**
     * Validate chuyển trạng thái
     */
    private void validateTrangThaiTransition(TrangThaiDonHang from, TrangThaiDonHang to) {
        // CHO_XU_LY -> DANG_GIAO, HUY
        // DANG_GIAO -> HOAN_THANH
        // HOAN_THANH, HUY -> không thể chuyển

        if (from == TrangThaiDonHang.HOAN_THANH || from == TrangThaiDonHang.HUY) {
            throw new IllegalStateException("Không thể thay đổi trạng thái đơn hàng đã hoàn thành hoặc đã hủy");
        }

        if (from == TrangThaiDonHang.CHO_XU_LY) {
            if (to != TrangThaiDonHang.DANG_GIAO && to != TrangThaiDonHang.HUY) {
                throw new IllegalStateException("Chỉ có thể chuyển sang Đang giao hoặc Hủy");
            }
        }

        if (from == TrangThaiDonHang.DANG_GIAO) {
            if (to != TrangThaiDonHang.HOAN_THANH) {
                throw new IllegalStateException("Chỉ có thể chuyển sang Hoàn thành");
            }
        }
    }

    /**
     * Chuyển đổi sang Response DTO
     */
    public DonHangResponse convertToResponse(DonHang donHang) {
        return DonHangResponse.fromEntity(donHang);
    }

    /**
     * Tính tổng doanh thu
     */
    public BigDecimal tinhTongDoanhThu() {
        BigDecimal total = donHangRepository.calculateTotalRevenue();
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Tính doanh thu theo thời gian
     */
    public BigDecimal tinhDoanhThuTheoThoiGian(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = donHangRepository.calculateRevenueByDateRange(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Đếm đơn hàng theo trạng thái
     */
    public long countByTrangThai(TrangThaiDonHang trangThai) {
        return donHangRepository.countByTrangThaiDonHang(trangThai);
    }
}