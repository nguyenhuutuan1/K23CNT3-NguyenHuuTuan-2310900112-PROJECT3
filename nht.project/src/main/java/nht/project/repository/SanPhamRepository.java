package nht.project.repository;

import nht.project.model.SanPham;
import nht.project.enums.TrangThaiSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {

    // Tìm theo tên sản phẩm
    Optional<SanPham> findByTenSanPham(String tenSanPham);

    // Tìm sản phẩm theo trạng thái
    List<SanPham> findByTrangThai(TrangThaiSanPham trangThai);

    // Tìm sản phẩm theo danh mục
    List<SanPham> findByDanhMucId(Long danhMucId);

    // Tìm sản phẩm theo danh mục và trạng thái
    List<SanPham> findByDanhMucIdAndTrangThai(Long danhMucId, TrangThaiSanPham trangThai);

    // Tìm sản phẩm có sẵn (AVAILABLE và còn hàng)
    @Query("SELECT sp FROM SanPham sp WHERE sp.trangThai = 'AVAILABLE' AND sp.soLuongTon > 0")
    List<SanPham> findAllAvailable();

    // Tìm sản phẩm có sẵn theo danh mục
    @Query("SELECT sp FROM SanPham sp WHERE sp.danhMuc.id = :danhMucId AND sp.trangThai = 'AVAILABLE' AND sp.soLuongTon > 0")
    List<SanPham> findAvailableByDanhMucId(@Param("danhMucId") Long danhMucId);

    // Tìm kiếm sản phẩm theo tên (có phân trang)
    Page<SanPham> findByTenSanPhamContainingIgnoreCase(String keyword, Pageable pageable);

    // Tìm kiếm sản phẩm theo tên hoặc mô tả
    @Query("SELECT sp FROM SanPham sp WHERE LOWER(sp.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(sp.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SanPham> searchByKeyword(@Param("keyword") String keyword);

    // Tìm sản phẩm theo khoảng giá
    List<SanPham> findByGiaBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Tìm sản phẩm theo khoảng giá và trạng thái
    @Query("SELECT sp FROM SanPham sp WHERE sp.gia BETWEEN :minPrice AND :maxPrice " +
            "AND sp.trangThai = :trangThai ORDER BY sp.gia ASC")
    List<SanPham> findByPriceRangeAndStatus(@Param("minPrice") BigDecimal minPrice,
                                            @Param("maxPrice") BigDecimal maxPrice,
                                            @Param("trangThai") TrangThaiSanPham trangThai);

    // Tìm sản phẩm sắp hết hàng (số lượng tồn < ngưỡng)
    @Query("SELECT sp FROM SanPham sp WHERE sp.soLuongTon <= :threshold AND sp.soLuongTon > 0")
    List<SanPham> findLowStockProducts(@Param("threshold") Integer threshold);

    // Tìm sản phẩm hết hàng
    @Query("SELECT sp FROM SanPham sp WHERE sp.soLuongTon = 0 OR sp.trangThai = 'OUT_OF_STOCK'")
    List<SanPham> findOutOfStockProducts();

    // Lấy sản phẩm mới nhất (theo ngày tạo)
    List<SanPham> findTop10ByTrangThaiOrderByNgayTaoDesc(TrangThaiSanPham trangThai);

    // Lấy sản phẩm bán chạy (có nhiều đơn hàng nhất)
    @Query("SELECT sp FROM SanPham sp JOIN sp.chiTietDonHangs ct " +
            "WHERE sp.trangThai = 'AVAILABLE' " +
            "GROUP BY sp.id ORDER BY SUM(ct.soLuong) DESC")
    List<SanPham> findBestSellingProducts(Pageable pageable);

    // Đếm số sản phẩm theo trạng thái
    long countByTrangThai(TrangThaiSanPham trangThai);

    // Đếm số sản phẩm có sẵn
    @Query("SELECT COUNT(sp) FROM SanPham sp WHERE sp.trangThai = 'AVAILABLE' AND sp.soLuongTon > 0")
    long countAvailableProducts();

    // Tính tổng giá trị tồn kho
    @Query("SELECT SUM(sp.gia * sp.soLuongTon) FROM SanPham sp WHERE sp.trangThai != 'DISCONTINUED'")
    BigDecimal calculateTotalInventoryValue();
}