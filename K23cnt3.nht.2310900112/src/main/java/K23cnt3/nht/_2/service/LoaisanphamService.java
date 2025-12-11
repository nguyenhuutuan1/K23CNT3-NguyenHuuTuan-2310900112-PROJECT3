package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Loaisanpham;
import java.util.List;
import java.util.Optional;

public interface LoaisanphamService {
    List<Loaisanpham> getAllLoaisanpham();
    Optional<Loaisanpham> getLoaisanphamById(Integer id);
    Loaisanpham saveLoaisanpham(Loaisanpham loaisanpham);
    void deleteLoaisanpham(Integer id);
}