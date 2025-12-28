package nht.project.repository;

import nht.project.model.ChiTietDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {

    // Tìm chi tiết theo đơn hàng
    List<ChiTietDonHang> findByDonHangId(Long donHangId);

    // Tìm chi tiết theo sản phẩm
    List<ChiTietDonHang> findBySanPhamId(Long sanPhamId);

    // Tìm chi tiết theo đơn hàng và sản phẩm
    Optional<ChiTietDonHang> findByDonHangIdAndSanPhamId(Long donHangId, Long sanPhamId);

    // Đếm số lần sản phẩm được mua
    @Query("SELECT COUNT(ct) FROM ChiTietDonHang ct WHERE ct.sanPham.id = :sanPhamId")
    long countBySanPhamId(@Param("sanPhamId") Long sanPhamId);

    // Tính tổng số lượng sản phẩm đã bán
    @Query("SELECT SUM(ct.soLuong) FROM ChiTietDonHang ct WHERE ct.sanPham.id = :sanPhamId")
    Long sumQuantityBySanPhamId(@Param("sanPhamId") Long sanPhamId);

    // Tính tổng doanh thu từ sản phẩm
    @Query("SELECT SUM(ct.thanhTien) FROM ChiTietDonHang ct WHERE ct.sanPham.id = :sanPhamId " +
            "AND ct.donHang.trangThaiDonHang = 'HOAN_THANH'")
    BigDecimal calculateRevenueBySanPhamId(@Param("sanPhamId") Long sanPhamId);

    // Lấy danh sách sản phẩm bán chạy nhất
    @Query("SELECT ct.sanPham.id, ct.sanPham.tenSanPham, SUM(ct.soLuong) as totalSold " +
            "FROM ChiTietDonHang ct " +
            "WHERE ct.donHang.trangThaiDonHang = 'HOAN_THANH' " +
            "GROUP BY ct.sanPham.id, ct.sanPham.tenSanPham " +
            "ORDER BY totalSold DESC")
    List<Object[]> findBestSellingProducts();

    // Lấy top sản phẩm bán chạy
    @Query("SELECT ct.sanPham.id, ct.sanPham.tenSanPham, SUM(ct.soLuong) as totalSold " +
            "FROM ChiTietDonHang ct " +
            "WHERE ct.donHang.trangThaiDonHang = 'HOAN_THANH' " +
            "GROUP BY ct.sanPham.id, ct.sanPham.tenSanPham " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts(@Param("limit") int limit);

    // Tính tổng giá trị đơn hàng từ chi tiết
    @Query("SELECT SUM(ct.thanhTien) FROM ChiTietDonHang ct WHERE ct.donHang.id = :donHangId")
    BigDecimal calculateTotalByDonHangId(@Param("donHangId") Long donHangId);

    // Đếm số mặt hàng trong đơn hàng
    @Query("SELECT COUNT(ct) FROM ChiTietDonHang ct WHERE ct.donHang.id = :donHangId")
    long countItemsByDonHangId(@Param("donHangId") Long donHangId);

    // Tìm các đơn hàng có chứa sản phẩm cụ thể
    @Query("SELECT DISTINCT ct.donHang FROM ChiTietDonHang ct WHERE ct.sanPham.id = :sanPhamId")
    List<Object> findDonHangsBySanPhamId(@Param("sanPhamId") Long sanPhamId);
}