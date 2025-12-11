package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Sanpham;
import java.util.Map;

public interface GioHangService {
    void themSanPhamVaoGio(Integer maSP, Integer soLuong);
    void capNhatSoLuong(Integer maSP, Integer soLuong);
    void xoaSanPhamKhoiGio(Integer maSP);
    void xoaToanBoGioHang();

    Map<Integer, Integer> getGioHang();
    int getTongSoLuong();
    double getTongTien();

    Sanpham getSanPhamById(Integer maSP);
    boolean kiemTraSanPhamConHang(Integer maSP, Integer soLuong);
}