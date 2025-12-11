package K23cnt3.nht._2.repository;

import K23cnt3.nht._2.entity.Chitiethoadon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChitiethoadonRepository extends JpaRepository<Chitiethoadon, Integer> {

    List<Chitiethoadon> findByHoadon_MaHD(Integer maHD);

    List<Chitiethoadon> findBySanpham_MaSP(Integer maSP);

    @Query("SELECT c FROM Chitiethoadon c WHERE c.hoadon.maHD = :maHD")
    List<Chitiethoadon> findChiTietByMaHD(@Param("maHD") Integer maHD);

    @Query("SELECT c.sanpham.maSP, SUM(c.soLuong) FROM Chitiethoadon c GROUP BY c.sanpham.maSP ORDER BY SUM(c.soLuong) DESC")
    List<Object[]> findSanPhamBanChay();
}