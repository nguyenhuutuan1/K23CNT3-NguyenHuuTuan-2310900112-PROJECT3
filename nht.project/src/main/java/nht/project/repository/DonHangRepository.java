package nht.project.repository;

import nht.project.model.DonHang;
import nht.project.enums.TrangThaiDonHang;
import nht.project.enums.TrangThaiThanhToan;
import nht.project.enums.PhuongThucThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Long> {

    // Tìm đơn hàng theo mã
    Optional<DonHang> findByMaDonHang(String maDonHang);

    // Tìm đơn hàng theo khách hàng
    List<DonHang> findByKhachHangId(Long khachHangId);

    // Tìm đơn hàng theo khách hàng (có phân trang)
    Page<DonHang> findByKhachHangId(Long khachHangId, Pageable pageable);

    // Tìm đơn hàng theo khách hàng và sắp xếp theo ngày đặt mới nhất
    List<DonHang> findByKhachHangIdOrderByNgayDatDesc(Long khachHangId);

    // Tìm đơn hàng theo trạng thái
    List<DonHang> findByTrangThaiDonHang(TrangThaiDonHang trangThaiDonHang);

    // Tìm đơn hàng theo trạng thái thanh toán
    List<DonHang> findByTrangThaiThanhToan(TrangThaiThanhToan trangThaiThanhToan);

    // Tìm đơn hàng theo khách hàng và trạng thái
    List<DonHang> findByKhachHangIdAndTrangThaiDonHang(Long khachHangId, TrangThaiDonHang trangThaiDonHang);

    // Tìm đơn hàng chờ xử lý
    @Query("SELECT dh FROM DonHang dh WHERE dh.trangThaiDonHang = 'CHO_XU_LY' ORDER BY dh.ngayDat ASC")
    List<DonHang> findPendingOrders();

    // Tìm đơn hàng chưa thanh toán
    @Query("SELECT dh FROM DonHang dh WHERE dh.trangThaiThanhToan = 'CHUA_THANH_TOAN' " +
            "AND dh.trangThaiDonHang != 'HUY' ORDER BY dh.ngayDat ASC")
    List<DonHang> findUnpaidOrders();

    // Tìm đơn hàng theo phương thức thanh toán
    List<DonHang> findByPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan);

    // Tìm đơn hàng trong khoảng thời gian
    @Query("SELECT dh FROM DonHang dh WHERE dh.ngayDat BETWEEN :startDate AND :endDate " +
            "ORDER BY dh.ngayDat DESC")
    List<DonHang> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    // Tìm đơn hàng theo tổng tiền
    List<DonHang> findByTongTienGreaterThanEqual(BigDecimal minAmount);

    // Đếm đơn hàng theo trạng thái
    long countByTrangThaiDonHang(TrangThaiDonHang trangThaiDonHang);

    // Đếm đơn hàng của khách hàng theo trạng thái
    long countByKhachHangIdAndTrangThaiDonHang(Long khachHangId, TrangThaiDonHang trangThaiDonHang);

    // Tính tổng doanh thu
    @Query("SELECT SUM(dh.tongTien) FROM DonHang dh WHERE dh.trangThaiDonHang = 'HOAN_THANH'")
    BigDecimal calculateTotalRevenue();

    // Tính tổng doanh thu theo thời gian
    @Query("SELECT SUM(dh.tongTien) FROM DonHang dh WHERE dh.trangThaiDonHang = 'HOAN_THANH' " +
            "AND dh.ngayDat BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueByDateRange(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    // Tính tổng doanh thu theo phương thức thanh toán
    @Query("SELECT SUM(dh.tongTien) FROM DonHang dh WHERE dh.trangThaiDonHang = 'HOAN_THANH' " +
            "AND dh.phuongThucThanhToan = :phuongThuc")
    BigDecimal calculateRevenueByPaymentMethod(@Param("phuongThuc") PhuongThucThanhToan phuongThuc);

    // Lấy đơn hàng mới nhất
    List<DonHang> findTop10ByOrderByNgayDatDesc();

    // Lấy đơn hàng có giá trị cao nhất
    List<DonHang> findTop10ByTrangThaiDonHangOrderByTongTienDesc(TrangThaiDonHang trangThaiDonHang);

    // Tìm đơn hàng theo ID với chi tiết đầy đủ (eager loading)
    @Query("SELECT DISTINCT dh FROM DonHang dh " +
            "LEFT JOIN FETCH dh.chiTietDonHangs ct " +
            "LEFT JOIN FETCH ct.sanPham " +
            "LEFT JOIN FETCH dh.khachHang " +
            "WHERE dh.id = :id")
    Optional<DonHang> findByIdWithDetails(@Param("id") Long id);

    // Tìm đơn hàng theo mã với chi tiết đầy đủ
    @Query("SELECT DISTINCT dh FROM DonHang dh " +
            "LEFT JOIN FETCH dh.chiTietDonHangs ct " +
            "LEFT JOIN FETCH ct.sanPham " +
            "LEFT JOIN FETCH dh.khachHang " +
            "WHERE dh.maDonHang = :maDonHang")
    Optional<DonHang> findByMaDonHangWithDetails(@Param("maDonHang") String maDonHang);
}