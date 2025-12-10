package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Chitiethoadon;
import K23cnt3.nht._2.repository.ChitiethoadonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ChitiethoadonService {

    @Autowired
    private ChitiethoadonRepository chitiethoadonRepository;

    public List<Chitiethoadon> getAllChitiethoadon() {
        return chitiethoadonRepository.findAll();
    }

    public Chitiethoadon getChitiethoadonById(Integer id) {
        Optional<Chitiethoadon> optional = chitiethoadonRepository.findById(id);
        return optional.orElse(null);
    }

    public Chitiethoadon saveChitiethoadon(Chitiethoadon chitiethoadon) {
        return chitiethoadonRepository.save(chitiethoadon);
    }

    public void deleteChitiethoadon(Integer id) {
        chitiethoadonRepository.deleteById(id);
    }

    public List<Chitiethoadon> getChiTietByHoaDonId(Integer maHD) {
        return chitiethoadonRepository.findChiTietByHoaDonId(maHD);
    }

    public Integer tongSoLuongDaBan(Integer maSP) {
        return chitiethoadonRepository.tongSoLuongDaBan(maSP);
    }
}