package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Chitiethoadon;
import K23cnt3.nht._2.entity.Hoadon;
import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.repository.ChitiethoadonRepository;
import K23cnt3.nht._2.repository.HoadonRepository;
import K23cnt3.nht._2.repository.SanphamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HoadonService {

    @Autowired
    private HoadonRepository hoadonRepository;

    @Autowired
    private SanphamRepository sanphamRepository;

    @Autowired
    private ChitiethoadonRepository chitiethoadonRepository;

    public List<Hoadon> getAllHoadon() {
        return hoadonRepository.findAll();
    }

    public Hoadon getHoadonById(Integer id) {
        Optional<Hoadon> optional = hoadonRepository.findById(id);
        return optional.orElse(null);
    }

    public Hoadon saveHoadon(Hoadon hoadon) {
        return hoadonRepository.save(hoadon);
    }

    @Transactional
    public Hoadon createHoadon(Hoadon hoadon, List<Chitiethoadon> chiTietList) {
        if (hoadon.getNgayLapHD() == null) {
            hoadon.setNgayLapHD(LocalDate.now());
        }

        if (hoadon.getTrangThai() == null) {
            hoadon.setTrangThai("Chờ xử lý");
        }

        Hoadon savedHoadon = hoadonRepository.save(hoadon);
        BigDecimal tongTien = BigDecimal.ZERO;

        for (Chitiethoadon chiTiet : chiTietList) {
            chiTiet.setHoaDon(savedHoadon);

            Sanpham sanpham = sanphamRepository.findById(chiTiet.getSanPham().getMaSP())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            if (sanpham.getSoLuong() < chiTiet.getSoLuong()) {
                throw new RuntimeException("Sản phẩm " + sanpham.getTenSP() + " không đủ hàng");
            }

            chiTiet.setDonGia(sanpham.getDonGia());
            chiTiet.calculateThanhTien();

            chitiethoadonRepository.save(chiTiet);

            sanpham.setSoLuong(sanpham.getSoLuong() - chiTiet.getSoLuong());
            sanphamRepository.save(sanpham);

            tongTien = tongTien.add(chiTiet.getThanhTien());
        }

        savedHoadon.setTongTien(tongTien);
        return hoadonRepository.save(savedHoadon);
    }

    @Transactional
    public void deleteHoadon(Integer id) {
        Optional<Hoadon> optional = hoadonRepository.findById(id);
        if (optional.isPresent()) {
            Hoadon hoadon = optional.get();

            List<Chitiethoadon> chiTietList = chitiethoadonRepository.findByHoaDon_MaHD(id);
            for (Chitiethoadon chiTiet : chiTietList) {
                Sanpham sanpham = chiTiet.getSanPham();
                if (sanpham != null) {
                    sanpham.setSoLuong(sanpham.getSoLuong() + chiTiet.getSoLuong());
                    sanphamRepository.save(sanpham);
                }
            }

            hoadonRepository.deleteById(id);
        }
    }

    public List<Hoadon> getHoadonByTrangThai(String trangThai) {
        return hoadonRepository.findByTrangThai(trangThai);
    }

    public Double getDoanhThuNgay(LocalDate date) {
        Double doanhThu = hoadonRepository.getDoanhThuNgay(date);
        return doanhThu != null ? doanhThu : 0.0;
    }

    public Integer countHoaDonNgay(LocalDate date) {
        Integer count = hoadonRepository.countHoaDonByNgay(date);
        return count != null ? count : 0;
    }
}