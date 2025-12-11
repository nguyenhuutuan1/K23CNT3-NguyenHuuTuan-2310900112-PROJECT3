package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Khachhang;
import java.util.List;
import java.util.Optional;

public interface KhachhangService {
    List<Khachhang> getAllKhachhang();
    Optional<Khachhang> getKhachhangById(Integer id);
    Khachhang saveKhachhang(Khachhang khachhang);
    void deleteKhachhang(Integer id);

    Khachhang getKhachhangByEmail(String email);
    Khachhang getKhachhangByDienThoai(String dienThoai);
}