package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Chitiethoadon;
import K23cnt3.nht._2.repository.ChitiethoadonRepository;
import K23cnt3.nht._2.service.ChitiethoadonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ChitiethoadonServiceImpl implements ChitiethoadonService {

    @Autowired
    private ChitiethoadonRepository chitiethoadonRepository;

    @Override
    public List<Chitiethoadon> getAllChitiethoadon() {
        return chitiethoadonRepository.findAll();
    }

    @Override
    public Optional<Chitiethoadon> getChitiethoadonById(Integer id) {
        return chitiethoadonRepository.findById(id);
    }

    @Override
    public Chitiethoadon saveChitiethoadon(Chitiethoadon chitiethoadon) {
        return chitiethoadonRepository.save(chitiethoadon);
    }

    @Override
    public void deleteChitiethoadon(Integer id) {
        chitiethoadonRepository.deleteById(id);
    }

    @Override
    public List<Chitiethoadon> getChitiethoadonByHoadon(Integer maHD) {
        return chitiethoadonRepository.findChiTietByMaHD(maHD);
    }

    @Override
    public List<Chitiethoadon> getChitiethoadonBySanpham(Integer maSP) {
        return chitiethoadonRepository.findBySanpham_MaSP(maSP);
    }

    @Override
    public List<Object[]> getSanPhamBanChay() {
        return chitiethoadonRepository.findSanPhamBanChay();
    }
}