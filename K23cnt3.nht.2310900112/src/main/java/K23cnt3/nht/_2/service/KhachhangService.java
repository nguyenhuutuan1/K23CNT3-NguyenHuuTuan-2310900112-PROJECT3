package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Khachhang;
import K23cnt3.nht._2.repository.KhachhangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class KhachhangService {

    @Autowired
    private KhachhangRepository khachhangRepository;

    public List<Khachhang> getAllKhachhang() {
        return khachhangRepository.findAll();
    }

    public Khachhang getKhachhangById(Integer id) {
        Optional<Khachhang> optional = khachhangRepository.findById(id);
        return optional.orElse(null);
    }

    public Khachhang saveKhachhang(Khachhang khachhang) {
        return khachhangRepository.save(khachhang);
    }

    public void deleteKhachhang(Integer id) {
        khachhangRepository.deleteById(id);
    }

    public List<Khachhang> searchKhachhang(String keyword) {
        return khachhangRepository.findByHoTenContaining(keyword);
    }
}