package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Sanpham;
import java.util.List;
import java.util.Optional;

public interface SanphamService {
    List<Sanpham> getAllSanpham();
    Optional<Sanpham> getSanphamById(Integer id);
    Sanpham saveSanpham(Sanpham sanpham);
    void deleteSanpham(Integer id);

    List<Sanpham> searchSanpham(String keyword);
    List<Sanpham> getSanphamByLoai(Integer maLoai);
    List<Sanpham> getSanphamConHang();
    List<Sanpham> getSanphamNoiBat();
}