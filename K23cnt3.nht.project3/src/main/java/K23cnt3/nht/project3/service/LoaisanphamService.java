package K23cnt3.nht.project3.service;

import K23cnt3.nht.project3.entity.Loaisanpham;
import K23cnt3.nht.project3.repository.LoaisanphamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoaisanphamService {

    @Autowired
    private LoaisanphamRepository loaisanphamRepository;

    public List<Loaisanpham> getAllLoaisanpham() {
        return loaisanphamRepository.findAll();
    }

    public Loaisanpham getLoaisanphamById(Integer id) {
        Optional<Loaisanpham> optional = loaisanphamRepository.findById(id);
        return optional.orElse(null);
    }

    public Loaisanpham saveLoaisanpham(Loaisanpham loaisanpham) {
        return loaisanphamRepository.save(loaisanpham);
    }

    public void deleteLoaisanpham(Integer id) {
        loaisanphamRepository.deleteById(id);
    }

    public List<Loaisanpham> searchLoaisanpham(String keyword) {
        return loaisanphamRepository.findByTenLoaiContaining(keyword);
    }
}