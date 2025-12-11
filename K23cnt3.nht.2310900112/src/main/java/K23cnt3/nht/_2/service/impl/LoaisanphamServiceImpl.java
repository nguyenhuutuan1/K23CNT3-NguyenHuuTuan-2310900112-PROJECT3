package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Loaisanpham;
import K23cnt3.nht._2.repository.LoaisanphamRepository;
import K23cnt3.nht._2.service.LoaisanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoaisanphamServiceImpl implements LoaisanphamService {

    @Autowired
    private LoaisanphamRepository loaisanphamRepository;

    @Override
    public List<Loaisanpham> getAllLoaisanpham() {
        return loaisanphamRepository.findAll();
    }

    @Override
    public Optional<Loaisanpham> getLoaisanphamById(Integer id) {
        return loaisanphamRepository.findById(id);
    }

    @Override
    public Loaisanpham saveLoaisanpham(Loaisanpham loaisanpham) {
        return loaisanphamRepository.save(loaisanpham);
    }

    @Override
    public void deleteLoaisanpham(Integer id) {
        loaisanphamRepository.deleteById(id);
    }
}