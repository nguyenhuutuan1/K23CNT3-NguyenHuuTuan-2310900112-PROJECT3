package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Nhanvien;
import java.util.List;
import java.util.Optional;

public interface NhanvienService {
    List<Nhanvien> getAllNhanvien();
    Optional<Nhanvien> getNhanvienById(Integer id);
    Nhanvien saveNhanvien(Nhanvien nhanvien);
    void deleteNhanvien(Integer id);

    Nhanvien getNhanvienByEmail(String email);
    Nhanvien getNhanvienByDienThoai(String dienThoai);
}