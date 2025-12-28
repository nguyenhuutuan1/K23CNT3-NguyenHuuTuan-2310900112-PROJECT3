package nht.project.repository;

import nht.project.model.ThanhToan;
import nht.project.enums.PhuongThucThanhToan;
import nht.project.enums.TrangThaiThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {

    // Tìm thanh toán theo mã giao dịch
    Optional<ThanhToan> findByMaGiaoDich(String maGiaoDich);

    // Tìm tất cả thanh toán của một đơn hàng
    List<ThanhToan> findByDonHangId(Long donHangId);

    // Tìm thanh toán theo đơn hàng và sắp xếp theo ngày
    List<ThanhToan> findByDonHangIdOrderByNgayThanhToanDesc(Long donHangId);

    // Tìm thanh toán theo trạng thái
    List<ThanhToan> findByTrangThai(TrangThaiThanhToan trangThai);

    // Tìm thanh toán theo phương thức
    List<ThanhToan> findByPhuongThuc(PhuongThucThanhToan phuongThuc);

    // Tìm thanh toán thành công của đơn hàng
    @Query("SELECT tt FROM ThanhToan tt WHERE tt.donHang.id = :donHangId " +
            "AND tt.trangThai = 'DA_THANH_TOAN'")
    List<ThanhToan> findSuccessfulPaymentsByDonHangId(@Param("donHangId") Long donHangId);

    // Kiểm tra đơn hàng đã được thanh toán chưa
    @Query("SELECT CASE WHEN COUNT(tt) > 0 THEN true ELSE false END FROM ThanhToan tt " +
            "WHERE tt.donHang.id = :donHangId AND tt.trangThai = 'DA_THANH_TOAN'")
    boolean existsSuccessfulPaymentByDonHangId(@Param("donHangId") Long donHangId);

    // Tính tổng số tiền đã thanh toán cho đơn hàng
    @Query("SELECT SUM(tt.soTien) FROM ThanhToan tt WHERE tt.donHang.id = :donHangId " +
            "AND tt.trangThai = 'DA_THANH_TOAN'")
    BigDecimal calculateTotalPaidByDonHangId(@Param("donHangId") Long donHangId);

    // Tìm thanh toán trong khoảng thời gian
    @Query("SELECT tt FROM ThanhToan tt WHERE tt.ngayThanhToan BETWEEN :startDate AND :endDate " +
            "ORDER BY tt.ngayThanhToan DESC")
    List<ThanhToan> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    // Tính tổng doanh thu theo phương thức thanh toán
    @Query("SELECT SUM(tt.soTien) FROM ThanhToan tt WHERE tt.phuongThuc = :phuongThuc " +
            "AND tt.trangThai = 'DA_THANH_TOAN'")
    BigDecimal calculateTotalByPaymentMethod(@Param("phuongThuc") PhuongThucThanhToan phuongThuc);

    // Tính tổng doanh thu theo phương thức và khoảng thời gian
    @Query("SELECT SUM(tt.soTien) FROM ThanhToan tt WHERE tt.phuongThuc = :phuongThuc " +
            "AND tt.trangThai = 'DA_THANH_TOAN' " +
            "AND tt.ngayThanhToan BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueByMethodAndDateRange(@Param("phuongThuc") PhuongThucThanhToan phuongThuc,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    // Đếm số lượng thanh toán theo trạng thái
    long countByTrangThai(TrangThaiThanhToan trangThai);

    // Đếm số lượng thanh toán theo phương thức
    long countByPhuongThuc(PhuongThucThanhToan phuongThuc);

    // Lấy thanh toán mới nhất
    List<ThanhToan> findTop10ByTrangThaiOrderByNgayThanhToanDesc(TrangThaiThanhToan trangThai);

    // Tìm thanh toán theo số tiền
    List<ThanhToan> findBySoTienGreaterThanEqual(BigDecimal minAmount);

    // Kiểm tra mã giao dịch đã tồn tại chưa
    boolean existsByMaGiaoDich(String maGiaoDich);
}