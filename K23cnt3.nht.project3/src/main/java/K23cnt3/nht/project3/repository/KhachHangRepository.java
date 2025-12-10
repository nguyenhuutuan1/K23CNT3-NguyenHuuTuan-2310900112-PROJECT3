package K23cnt3.nht.project3.repository;

import K23cnt3.nht.project3.entity.Khachhang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KhachhangRepository extends JpaRepository<Khachhang, Integer> {

    List<Khachhang> findByHoTenContaining(String hoTen);

    List<Khachhang> findByEmail(String email);

    List<Khachhang> findByDienThoai(String dienThoai);
}