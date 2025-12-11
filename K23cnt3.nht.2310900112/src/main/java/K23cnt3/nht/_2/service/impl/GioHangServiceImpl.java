package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.service.GioHangService;
import K23cnt3.nht._2.service.SanphamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private HttpSession session;

    private static final String GIO_HANG_SESSION_KEY = "gioHang";

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getGioHangFromSession() {
        Map<Integer, Integer> gioHang = (Map<Integer, Integer>) session.getAttribute(GIO_HANG_SESSION_KEY);
        if (gioHang == null) {
            gioHang = new HashMap<>();
            session.setAttribute(GIO_HANG_SESSION_KEY, gioHang);
        }
        return gioHang;
    }

    private void saveGioHangToSession(Map<Integer, Integer> gioHang) {
        session.setAttribute(GIO_HANG_SESSION_KEY, gioHang);
    }

    @Override
    public void themSanPhamVaoGio(Integer maSP, Integer soLuong) {
        Map<Integer, Integer> gioHang = getGioHangFromSession();

        if (gioHang.containsKey(maSP)) {
            int soLuongHienTai = gioHang.get(maSP);
            gioHang.put(maSP, soLuongHienTai + soLuong);
        } else {
            gioHang.put(maSP, soLuong);
        }

        saveGioHangToSession(gioHang);
    }

    @Override
    public void capNhatSoLuong(Integer maSP, Integer soLuong) {
        Map<Integer, Integer> gioHang = getGioHangFromSession();

        if (soLuong <= 0) {
            gioHang.remove(maSP);
        } else {
            gioHang.put(maSP, soLuong);
        }

        saveGioHangToSession(gioHang);
    }

    @Override
    public void xoaSanPhamKhoiGio(Integer maSP) {
        Map<Integer, Integer> gioHang = getGioHangFromSession();
        gioHang.remove(maSP);
        saveGioHangToSession(gioHang);
    }

    @Override
    public void xoaToanBoGioHang() {
        Map<Integer, Integer> gioHang = new HashMap<>();
        saveGioHangToSession(gioHang);
    }

    @Override
    public Map<Integer, Integer> getGioHang() {
        return getGioHangFromSession();
    }

    @Override
    public int getTongSoLuong() {
        Map<Integer, Integer> gioHang = getGioHangFromSession();
        return gioHang.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public double getTongTien() {
        Map<Integer, Integer> gioHang = getGioHangFromSession();
        double tongTien = 0.0;

        for (Map.Entry<Integer, Integer> entry : gioHang.entrySet()) {
            Sanpham sanpham = getSanPhamById(entry.getKey());
            if (sanpham != null && sanpham.getDonGia() != null) {
                tongTien += sanpham.getDonGia().doubleValue() * entry.getValue();
            }
        }

        return tongTien;
    }

    @Override
    public Sanpham getSanPhamById(Integer maSP) {
        return sanphamService.getSanphamById(maSP).orElse(null);
    }

    @Override
    public boolean kiemTraSanPhamConHang(Integer maSP, Integer soLuong) {
        Sanpham sanpham = getSanPhamById(maSP);
        return sanpham != null && sanpham.getSoLuong() != null && sanpham.getSoLuong() >= soLuong;
    }
}