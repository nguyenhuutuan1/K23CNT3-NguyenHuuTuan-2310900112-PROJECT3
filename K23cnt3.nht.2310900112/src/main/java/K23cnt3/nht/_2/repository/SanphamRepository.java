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

    List<Sanpham> findByLoaiSanPham_MaLoai(Integer maLoai);

    List<Sanpham> findBySoLuongGreaterThan(Integer soLuong);

    @Query("SELECT s FROM Sanpham s WHERE s.donGia BETWEEN :min AND :max")
    List<Sanpham> findByDonGiaBetween(@Param("min") Double min, @Param("max") Double max);

    @Query("SELECT s FROM Sanpham s WHERE s.nhaCungCap.maNCC = :maNCC")
    List<Sanpham> findByNhaCungCap(@Param("maNCC") Integer maNCC);
}