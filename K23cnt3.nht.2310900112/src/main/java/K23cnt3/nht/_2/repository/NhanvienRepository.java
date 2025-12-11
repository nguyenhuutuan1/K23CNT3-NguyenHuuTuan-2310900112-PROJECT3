package K23cnt3.nht._2.repository;

import K23cnt3.nht._2.entity.Nhanvien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanvienRepository extends JpaRepository<Nhanvien, Integer> {

    Nhanvien findByEmail(String email);

    Nhanvien findByDienThoai(String dienThoai);
}