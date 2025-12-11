package K23cnt3.nht._2.service;

import K23cnt3.nht._2.entity.Nhacungcap;
import java.util.List;
import java.util.Optional;

public interface NhacungcapService {
    List<Nhacungcap> getAllNhacungcap();
    Optional<Nhacungcap> getNhacungcapById(Integer id);
    Nhacungcap saveNhacungcap(Nhacungcap nhacungcap);
    void deleteNhacungcap(Integer id);
}