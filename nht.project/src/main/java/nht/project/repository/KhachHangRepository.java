package nht.project.repository;

import nht.project.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    // Tìm khách hàng theo email
    Optional<KhachHang> findByEmail(String email);

    // Tìm khách hàng theo số điện thoại
    Optional<KhachHang> findBySoDienThoai(String soDienThoai);

    // Tìm khách hàng theo email và mật khẩu (dùng cho login)
    Optional<KhachHang> findByEmailAndMatKhau(String email, String matKhau);

    // Tìm khách hàng theo tên (có thể một phần)
    List<KhachHang> findByHoTenContainingIgnoreCase(String hoTen);

    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    // Kiểm tra số điện thoại đã tồn tại chưa
    boolean existsBySoDienThoai(String soDienThoai);

    // Tìm kiếm khách hàng theo nhiều tiêu chí
    @Query("SELECT kh FROM KhachHang kh WHERE " +
            "LOWER(kh.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(kh.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "kh.soDienThoai LIKE CONCAT('%', :keyword, '%')")
    List<KhachHang> searchByKeyword(@Param("keyword") String keyword);

    // Lấy khách hàng có nhiều đơn hàng nhất
    @Query("SELECT kh FROM KhachHang kh JOIN kh.danhSachDonHang dh " +
            "GROUP BY kh.id ORDER BY COUNT(dh) DESC")
    List<KhachHang> findTopCustomers();

    // Đếm số đơn hàng của khách hàng
    @Query("SELECT COUNT(dh) FROM DonHang dh WHERE dh.khachHang.id = :khachHangId")
    long countOrdersByKhachHangId(@Param("khachHangId") Long khachHangId);

    // Lấy khách hàng mới đăng ký (top 10)
    List<KhachHang> findTop10ByOrderByNgayTaoDesc();
}