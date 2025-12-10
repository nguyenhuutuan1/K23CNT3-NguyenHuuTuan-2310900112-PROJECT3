package K23cnt3.nht.project3.repository;

import K23cnt3.nht.project3.entity.Chitiethoadon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChitiethoadonRepository extends JpaRepository<Chitiethoadon, Integer> {

    List<Chitiethoadon> findByHoaDon_MaHD(Integer maHD);

    List<Chitiethoadon> findBySanPham_MaSP(Integer maSP);

    @Query("SELECT c FROM Chitiethoadon c WHERE c.hoaDon.maHD = :maHD")
    List<Chitiethoadon> findChiTietByHoaDonId(@Param("maHD") Integer maHD);

    @Query("SELECT SUM(c.soLuong) FROM Chitiethoadon c WHERE c.sanPham.maSP = :maSP")
    Integer tongSoLuongDaBan(@Param("maSP") Integer maSP);
}