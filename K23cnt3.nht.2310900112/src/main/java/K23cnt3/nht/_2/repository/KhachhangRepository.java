package K23cnt3.nht._2.repository;

import K23cnt3.nht._2.entity.Khachhang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachhangRepository extends JpaRepository<Khachhang, Integer> {

    Khachhang findByEmail(String email);

    Khachhang findByDienThoai(String dienThoai);
}