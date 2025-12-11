package K23cnt3.nht._2.repository;

import K23cnt3.nht._2.entity.Sanpham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SanphamRepository extends JpaRepository<Sanpham, Integer> {

    List<Sanpham> findByTenSPContaining(String tenSP);

    List<Sanpham> findByLoaisanpham_MaLoai(Integer maLoai);

    List<Sanpham> findByNhacungcap_MaNCC(Integer maNCC);

    @Query("SELECT s FROM Sanpham s WHERE s.soLuong > 0 ORDER BY s.maSP DESC")
    List<Sanpham> findSanPhamConHang();

    @Query("SELECT s FROM Sanpham s WHERE s.tenSP LIKE %:keyword% OR s.moTa LIKE %:keyword%")
    List<Sanpham> timKiemSanPham(@Param("keyword") String keyword);
}