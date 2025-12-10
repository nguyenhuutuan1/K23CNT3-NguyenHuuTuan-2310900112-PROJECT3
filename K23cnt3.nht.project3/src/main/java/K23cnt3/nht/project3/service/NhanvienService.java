package K23cnt3.nht.project3.service;

import K23cnt3.nht.project3.entity.Nhanvien;
import K23cnt3.nht.project3.repository.NhanvienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NhanvienService {

    @Autowired
    private NhanvienRepository nhanvienRepository;

    public List<Nhanvien> getAllNhanvien() {
        return nhanvienRepository.findAll();
    }

    public Nhanvien getNhanvienById(Integer id) {
        Optional<Nhanvien> optional = nhanvienRepository.findById(id);
        return optional.orElse(null);
    }

    public Nhanvien saveNhanvien(Nhanvien nhanvien) {
        return nhanvienRepository.save(nhanvien);
    }

    public void deleteNhanvien(Integer id) {
        nhanvienRepository.deleteById(id);
    }

    public List<Nhanvien> searchNhanvien(String keyword) {
        return nhanvienRepository.findByHoTenContaining(keyword);
    }

    public List<Nhanvien> getNhanvienByChucVu(String chucVu) {
        return nhanvienRepository.findByChucVu(chucVu);
    }
}