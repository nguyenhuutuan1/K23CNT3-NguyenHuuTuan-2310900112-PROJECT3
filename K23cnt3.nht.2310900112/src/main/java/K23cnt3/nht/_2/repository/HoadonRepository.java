package K23cnt3.nht._2.repository;

import K23cnt3.nht._2.entity.Hoadon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoadonRepository extends JpaRepository<Hoadon, Integer> {

    List<Hoadon> findByKhachhang_MaKH(Integer maKH);

    List<Hoadon> findByNhanvien_MaNV(Integer maNV);

    List<Hoadon> findByTrangThai(String trangThai);

    List<Hoadon> findByNgayLapHD(LocalDate ngayLapHD);

    @Query("SELECT h FROM Hoadon h WHERE h.ngayLapHD BETWEEN :startDate AND :endDate")
    List<Hoadon> findHoaDonBetweenDates(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(h.tongTien) FROM Hoadon h WHERE h.trangThai = 'Đã thanh toán'")
    Double tongDoanhThu();
}