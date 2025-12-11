package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Nhanvien;
import K23cnt3.nht._2.repository.NhanvienRepository;
import K23cnt3.nht._2.service.NhanvienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NhanvienServiceImpl implements NhanvienService {

    @Autowired
    private NhanvienRepository nhanvienRepository;

    @Override
    public List<Nhanvien> getAllNhanvien() {
        return nhanvienRepository.findAll();
    }

    @Override
    public Optional<Nhanvien> getNhanvienById(Integer id) {
        return nhanvienRepository.findById(id);
    }

    @Override
    public Nhanvien saveNhanvien(Nhanvien nhanvien) {
        return nhanvienRepository.save(nhanvien);
    }

    @Override
    public void deleteNhanvien(Integer id) {
        nhanvienRepository.deleteById(id);
    }

    @Override
    public Nhanvien getNhanvienByEmail(String email) {
        return nhanvienRepository.findByEmail(email);
    }

    @Override
    public Nhanvien getNhanvienByDienThoai(String dienThoai) {
        return nhanvienRepository.findByDienThoai(dienThoai);
    }
}