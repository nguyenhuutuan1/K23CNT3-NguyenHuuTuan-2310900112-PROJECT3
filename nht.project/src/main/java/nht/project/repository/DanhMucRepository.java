package nht.project.repository;

import nht.project.model.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {

    // Tìm theo tên danh mục
    Optional<DanhMuc> findByTenDanhMuc(String tenDanhMuc);

    // Tìm danh mục có chứa từ khóa trong tên
    List<DanhMuc> findByTenDanhMucContainingIgnoreCase(String keyword);

    // Kiểm tra danh mục có tồn tại theo tên
    boolean existsByTenDanhMuc(String tenDanhMuc);

    // Lấy tất cả danh mục có sản phẩm
    @Query("SELECT DISTINCT d FROM DanhMuc d JOIN d.danhSachSanPham sp WHERE sp.trangThai = 'AVAILABLE'")
    List<DanhMuc> findAllWithAvailableProducts();

    // Đếm số lượng sản phẩm trong danh mục
    @Query("SELECT COUNT(sp) FROM SanPham sp WHERE sp.danhMuc.id = :danhMucId")
    long countProductsByDanhMucId(Long danhMucId);

    // Lấy danh mục theo thứ tự tên
    List<DanhMuc> findAllByOrderByTenDanhMucAsc();
}