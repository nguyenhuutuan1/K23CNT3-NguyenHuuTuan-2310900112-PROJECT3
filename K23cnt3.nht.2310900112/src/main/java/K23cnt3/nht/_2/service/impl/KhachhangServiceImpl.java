package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Khachhang;
import K23cnt3.nht._2.repository.KhachhangRepository;
import K23cnt3.nht._2.service.KhachhangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class KhachhangServiceImpl implements KhachhangService {

    @Autowired
    private KhachhangRepository khachhangRepository;

    @Override
    public List<Khachhang> getAllKhachhang() {
        return khachhangRepository.findAll();
    }

    @Override
    public Optional<Khachhang> getKhachhangById(Integer id) {
        return khachhangRepository.findById(id);
    }

    @Override
    public Khachhang saveKhachhang(Khachhang khachhang) {
        return khachhangRepository.save(khachhang);
    }

    @Override
    public void deleteKhachhang(Integer id) {
        khachhangRepository.deleteById(id);
    }

    @Override
    public Khachhang getKhachhangByEmail(String email) {
        return khachhangRepository.findByEmail(email);
    }

    @Override
    public Khachhang getKhachhangByDienThoai(String dienThoai) {
        return khachhangRepository.findByDienThoai(dienThoai);
    }
}