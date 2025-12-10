package K23cnt3.nht.project3.repository;

import K23cnt3.nht.project3.entity.Loaisanpham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoaisanphamRepository extends JpaRepository<Loaisanpham, Integer> {

    List<Loaisanpham> findByTenLoaiContaining(String tenLoai);
}