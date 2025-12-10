package K23cnt3.nht._2.repository;

import K23cnt3.nht._2.entity.Nhacungcap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NhacungcapRepository extends JpaRepository<Nhacungcap, Integer> {

    List<Nhacungcap> findByTenNCCContaining(String tenNCC);

    List<Nhacungcap> findByEmail(String email);

    List<Nhacungcap> findByDienThoai(String dienThoai);
}