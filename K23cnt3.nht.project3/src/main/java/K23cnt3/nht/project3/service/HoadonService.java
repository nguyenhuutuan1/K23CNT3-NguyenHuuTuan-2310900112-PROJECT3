package K23cnt3.nht.project3.service;

import K23cnt3.nht.project3.entity.Chitiethoadon;
import K23cnt3.nht.project3.entity.Hoadon;
import K23cnt3.nht.project3.entity.Sanpham;
import K23cnt3.nht.project3.repository.ChitiethoadonRepository;
import K23cnt3.nht.project3.repository.HoadonRepository;
import K23cnt3.nht.project3.repository.SanphamRepository;
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

    public Hoadon saveHoadon(Hoadon hoadon) {  // THÊM METHOD NÀY
        return hoadonRepository.save(hoadon);
    }

    @Transactional
    public Hoadon createHoadon(Hoadon hoadon, List<Chitiethoadon> chiTietList) {
        // Set ngày lập hóa đơn
        if (hoadon.getNgayLapHD() == null) {
            hoadon.setNgayLapHD(LocalDate.now());
        }

        // Set trạng thái mặc định
        if (hoadon.getTrangThai() == null) {
            hoadon.setTrangThai("Chờ xử lý");
        }

        // Lưu hóa đơn trước
        Hoadon savedHoadon = hoadonRepository.save(hoadon);

        // Tính tổng tiền
        BigDecimal tongTien = BigDecimal.ZERO;

        // Lưu chi tiết hóa đơn và cập nhật tồn kho
        for (Chitiethoadon chiTiet : chiTietList) {
            chiTiet.setHoaDon(savedHoadon);

            // Lấy thông tin sản phẩm
            Sanpham sanpham = sanphamRepository.findById(chiTiet.getSanPham().getMaSP())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Kiểm tra tồn kho
            if (sanpham.getSoLuong() < chiTiet.getSoLuong()) {
                throw new RuntimeException("Sản phẩm " + sanpham.getTenSP() + " không đủ hàng");
            }

            // Set đơn giá từ sản phẩm
            chiTiet.setDonGia(sanpham.getDonGia());

            // GỌI PHƯƠNG THỨC PUBLIC THAY VÌ PRIVATE
            chiTiet.calculateThanhTien();

            // Lưu chi tiết
            chitiethoadonRepository.save(chiTiet);

            // Cập nhật tồn kho
            sanpham.setSoLuong(sanpham.getSoLuong() - chiTiet.getSoLuong());
            sanphamRepository.save(sanpham);

            // Cộng vào tổng tiền
            tongTien = tongTien.add(chiTiet.getThanhTien());
        }

        // Cập nhật tổng tiền hóa đơn
        savedHoadon.setTongTien(tongTien);
        return hoadonRepository.save(savedHoadon);
    }

    @Transactional
    public void deleteHoadon(Integer id) {
        Optional<Hoadon> optional = hoadonRepository.findById(id);
        if (optional.isPresent()) {
            Hoadon hoadon = optional.get();

            // Khôi phục tồn kho nếu cần
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