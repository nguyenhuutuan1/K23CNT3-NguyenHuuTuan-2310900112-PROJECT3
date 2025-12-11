package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Hoadon;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HoadonService {
    List<Hoadon> getAllHoadon();
    Optional<Hoadon> getHoadonById(Integer id);
    Hoadon saveHoadon(Hoadon hoadon);
    void deleteHoadon(Integer id);

    List<Hoadon> getHoadonByKhachhang(Integer maKH);
    List<Hoadon> getHoadonByNhanvien(Integer maNV);
    List<Hoadon> getHoadonByTrangThai(String trangThai);
    List<Hoadon> getHoadonByNgay(LocalDate ngay);
    List<Hoadon> getHoadonBetweenDates(LocalDate startDate, LocalDate endDate);

    Double tinhTongDoanhThu();
}