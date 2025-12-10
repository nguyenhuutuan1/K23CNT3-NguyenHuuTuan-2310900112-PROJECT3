package K23cnt3.nht.project3.repository;

import K23cnt3.nht.project3.entity.Hoadon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoadonRepository extends JpaRepository<Hoadon, Integer> {

    List<Hoadon> findByTrangThai(String trangThai);

    List<Hoadon> findByNgayLapHDBetween(LocalDate startDate, LocalDate endDate);

    List<Hoadon> findByKhachHang_MaKH(Integer maKH);

    @Query("SELECT SUM(h.tongTien) FROM Hoadon h WHERE h.ngayLapHD = :date AND h.trangThai = 'Đã thanh toán'")
    Double getDoanhThuNgay(@Param("date") LocalDate date);

    @Query("SELECT COUNT(h) FROM Hoadon h WHERE h.ngayLapHD = :date")
    Integer countHoaDonByNgay(@Param("date") LocalDate date);
}