package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Nhacungcap;
import K23cnt3.nht._2.repository.NhacungcapRepository;
import K23cnt3.nht._2.service.NhacungcapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NhacungcapServiceImpl implements NhacungcapService {

    @Autowired
    private NhacungcapRepository nhacungcapRepository;

    @Override
    public List<Nhacungcap> getAllNhacungcap() {
        return nhacungcapRepository.findAll();
    }

    @Override
    public Optional<Nhacungcap> getNhacungcapById(Integer id) {
        return nhacungcapRepository.findById(id);
    }

    @Override
    public Nhacungcap saveNhacungcap(Nhacungcap nhacungcap) {
        return nhacungcapRepository.save(nhacungcap);
    }

    @Override
    public void deleteNhacungcap(Integer id) {
        nhacungcapRepository.deleteById(id);
    }
}