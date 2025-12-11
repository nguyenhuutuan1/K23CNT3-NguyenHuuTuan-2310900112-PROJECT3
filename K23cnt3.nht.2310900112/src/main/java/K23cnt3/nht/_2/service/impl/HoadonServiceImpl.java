package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Hoadon;
import K23cnt3.nht._2.repository.HoadonRepository;
import K23cnt3.nht._2.service.HoadonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HoadonServiceImpl implements HoadonService {

    @Autowired
    private HoadonRepository hoadonRepository;

    @Override
    public List<Hoadon> getAllHoadon() {
        return hoadonRepository.findAll();
    }

    @Override
    public Optional<Hoadon> getHoadonById(Integer id) {
        return hoadonRepository.findById(id);
    }

    @Override
    public Hoadon saveHoadon(Hoadon hoadon) {
        return hoadonRepository.save(hoadon);
    }

    @Override
    public void deleteHoadon(Integer id) {
        hoadonRepository.deleteById(id);
    }

    @Override
    public List<Hoadon> getHoadonByKhachhang(Integer maKH) {
        return hoadonRepository.findByKhachhang_MaKH(maKH);
    }

    @Override
    public List<Hoadon> getHoadonByNhanvien(Integer maNV) {
        return hoadonRepository.findByNhanvien_MaNV(maNV);
    }

    @Override
    public List<Hoadon> getHoadonByTrangThai(String trangThai) {
        return hoadonRepository.findByTrangThai(trangThai);
    }

    @Override
    public List<Hoadon> getHoadonByNgay(LocalDate ngay) {
        return hoadonRepository.findByNgayLapHD(ngay);
    }

    @Override
    public List<Hoadon> getHoadonBetweenDates(LocalDate startDate, LocalDate endDate) {
        return hoadonRepository.findHoaDonBetweenDates(startDate, endDate);
    }

    @Override
    public Double tinhTongDoanhThu() {
        Double doanhThu = hoadonRepository.tongDoanhThu();
        return doanhThu != null ? doanhThu : 0.0;
    }
}