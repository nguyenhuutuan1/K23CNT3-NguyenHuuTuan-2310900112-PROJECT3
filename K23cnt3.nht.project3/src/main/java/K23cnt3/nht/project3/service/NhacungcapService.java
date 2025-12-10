package K23cnt3.nht.project3.service;

import K23cnt3.nht.project3.entity.Nhacungcap;
import K23cnt3.nht.project3.repository.NhacungcapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NhacungcapService {

    @Autowired
    private NhacungcapRepository nhacungcapRepository;

    public List<Nhacungcap> getAllNhacungcap() {
        return nhacungcapRepository.findAll();
    }

    public Nhacungcap getNhacungcapById(Integer id) {
        Optional<Nhacungcap> optional = nhacungcapRepository.findById(id);
        return optional.orElse(null);
    }

    public Nhacungcap saveNhacungcap(Nhacungcap nhacungcap) {
        return nhacungcapRepository.save(nhacungcap);
    }

    public void deleteNhacungcap(Integer id) {
        nhacungcapRepository.deleteById(id);
    }

    public List<Nhacungcap> searchNhacungcap(String keyword) {
        return nhacungcapRepository.findByTenNCCContaining(keyword);
    }
}