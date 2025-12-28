package nht.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.dto.ThanhToanRequest;
import nht.project.enums.PhuongThucThanhToan;
import nht.project.enums.TrangThaiDonHang;
import nht.project.enums.TrangThaiThanhToan;
import nht.project.model.DonHang;
import nht.project.model.ThanhToan;
import nht.project.repository.DonHangRepository;
import nht.project.repository.ThanhToanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThanhToanService {

    private final ThanhToanRepository thanhToanRepository;
    private final DonHangRepository donHangRepository;

    /**
     * Lấy tất cả thanh toán
     */
    public List<ThanhToan> getAllThanhToan() {
        log.info("Lấy tất cả thanh toán");
        return thanhToanRepository.findAll();
    }

    /**
     * Lấy thanh toán theo ID
     */
    public Optional<ThanhToan> getThanhToanById(Long id) {
        log.info("Lấy thanh toán ID: {}", id);
        return thanhToanRepository.findById(id);
    }

    /**
     * Lấy thanh toán theo mã giao dịch
     */
    public Optional<ThanhToan> getThanhToanByMaGiaoDich(String maGiaoDich) {
        log.info("Lấy thanh toán mã GD: {}", maGiaoDich);
        return thanhToanRepository.findByMaGiaoDich(maGiaoDich);
    }

    /**
     * Lấy thanh toán theo đơn hàng
     */
    public List<ThanhToan> getThanhToanByDonHang(Long donHangId) {
        log.info("Lấy thanh toán của đơn hàng ID: {}", donHangId);
        return thanhToanRepository.findByDonHangIdOrderByNgayThanhToanDesc(donHangId);
    }

    /**
     * Xử lý thanh toán
     */
    @Transactional
    public ThanhToan xuLyThanhToan(ThanhToanRequest request) {
        log.info("Xử lý thanh toán cho đơn hàng: {}",
                request.getDonHangId() != null ? request.getDonHangId() : request.getMaDonHang());

        // Validate request
        if (!request.isValid()) {
            throw new IllegalArgumentException(request.getValidationMessage());
        }

        // Tìm đơn hàng
        DonHang donHang;
        if (request.getDonHangId() != null) {
            donHang = donHangRepository.findById(request.getDonHangId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
        } else {
            donHang = donHangRepository.findByMaDonHang(request.getMaDonHang())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
        }

        // Kiểm tra đơn hàng có thể thanh toán không
        if (!donHang.coTheThanhToan()) {
            throw new IllegalStateException("Đơn hàng không thể thanh toán");
        }

        // Kiểm tra số tiền
        if (request.getSoTien().compareTo(donHang.getTongTien()) != 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không khớp với tổng đơn hàng");
        }

        // Tạo thanh toán
        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setDonHang(donHang);
        thanhToan.setSoTien(request.getSoTien());
        thanhToan.setPhuongThuc(request.getPhuongThuc());
        thanhToan.setTrangThai(TrangThaiThanhToan.DA_THANH_TOAN);

        // Xử lý theo phương thức
        if (request.getPhuongThuc() == PhuongThucThanhToan.CHUYEN_KHOAN) {
            // Chuyển khoản - lưu thông tin giao dịch
            thanhToan.setMaGiaoDich(request.getMaGiaoDich());
        } else {
            // Tiền mặt - tự động tạo mã giao dịch
            // Mã GD sẽ được tạo trong @PrePersist
        }

        ThanhToan savedThanhToan = thanhToanRepository.save(thanhToan);

        // Cập nhật trạng thái đơn hàng
        donHang.setTrangThaiThanhToan(TrangThaiThanhToan.DA_THANH_TOAN);
        donHangRepository.save(donHang);

        log.info("Đã xử lý thanh toán - Mã GD: {}", savedThanhToan.getMaGiaoDich());
        return savedThanhToan;
    }

    /**
     * Thanh toán tiền mặt
     */
    @Transactional
    public ThanhToan thanhToanTienMat(Long donHangId, BigDecimal soTien) {
        log.info("Thanh toán tiền mặt cho đơn hàng ID: {}", donHangId);

        ThanhToanRequest request = new ThanhToanRequest();
        request.setDonHangId(donHangId);
        request.setSoTien(soTien);
        request.setPhuongThuc(PhuongThucThanhToan.TIEN_MAT);

        return xuLyThanhToan(request);
    }

    /**
     * Thanh toán chuyển khoản
     */
    @Transactional
    public ThanhToan thanhToanChuyenKhoan(Long donHangId, BigDecimal soTien, String maGiaoDich) {
        log.info("Thanh toán chuyển khoản cho đơn hàng ID: {} - Mã GD: {}", donHangId, maGiaoDich);

        // Kiểm tra mã giao dịch đã tồn tại chưa
        if (thanhToanRepository.existsByMaGiaoDich(maGiaoDich)) {
            throw new IllegalArgumentException("Mã giao dịch đã tồn tại");
        }

        ThanhToanRequest request = new ThanhToanRequest();
        request.setDonHangId(donHangId);
        request.setSoTien(soTien);
        request.setPhuongThuc(PhuongThucThanhToan.CHUYEN_KHOAN);
        request.setMaGiaoDich(maGiaoDich);

        return xuLyThanhToan(request);
    }

    /**
     * Kiểm tra đơn hàng đã thanh toán chưa
     */
    public boolean kiemTraDaThanhToan(Long donHangId) {
        return thanhToanRepository.existsSuccessfulPaymentByDonHangId(donHangId);
    }

    /**
     * Tính tổng số tiền đã thanh toán cho đơn hàng
     */
    public BigDecimal tinhTongDaThanhToan(Long donHangId) {
        BigDecimal total = thanhToanRepository.calculateTotalPaidByDonHangId(donHangId);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Lấy thanh toán theo khoảng thời gian
     */
    public List<ThanhToan> getThanhToanByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Lấy thanh toán từ {} đến {}", startDate, endDate);
        return thanhToanRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Tính tổng doanh thu theo phương thức thanh toán
     */
    public BigDecimal tinhDoanhThuTheoPhuongThuc(PhuongThucThanhToan phuongThuc) {
        BigDecimal total = thanhToanRepository.calculateTotalByPaymentMethod(phuongThuc);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Tính doanh thu theo phương thức và thời gian
     */
    public BigDecimal tinhDoanhThuTheoPhuongThucVaThoiGian(PhuongThucThanhToan phuongThuc,
                                                            LocalDateTime startDate,
                                                            LocalDateTime endDate) {
        BigDecimal total = thanhToanRepository.calculateRevenueByMethodAndDateRange(
                phuongThuc, startDate, endDate
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Đếm thanh toán theo trạng thái
     */
    public long countByTrangThai(TrangThaiThanhToan trangThai) {
        return thanhToanRepository.countByTrangThai(trangThai);
    }

    /**
     * Đếm thanh toán theo phương thức
     */
    public long countByPhuongThuc(PhuongThucThanhToan phuongThuc) {
        return thanhToanRepository.countByPhuongThuc(phuongThuc);
    }

    /**
     * Lấy thanh toán mới nhất
     */
    public List<ThanhToan> getLatestPayments(int limit) {
        log.info("Lấy {} thanh toán mới nhất", limit);
        return thanhToanRepository.findTop10ByTrangThaiOrderByNgayThanhToanDesc(
                TrangThaiThanhToan.DA_THANH_TOAN
        );
    }
}