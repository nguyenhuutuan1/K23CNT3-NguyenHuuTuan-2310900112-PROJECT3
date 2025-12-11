package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Chitiethoadon;
import java.util.List;
import java.util.Optional;

public interface ChitiethoadonService {
    List<Chitiethoadon> getAllChitiethoadon();
    Optional<Chitiethoadon> getChitiethoadonById(Integer id);
    Chitiethoadon saveChitiethoadon(Chitiethoadon chitiethoadon);
    void deleteChitiethoadon(Integer id);

    List<Chitiethoadon> getChitiethoadonByHoadon(Integer maHD);
    List<Chitiethoadon> getChitiethoadonBySanpham(Integer maSP);

    List<Object[]> getSanPhamBanChay();
}