package K23cnt3.nht.project3.repository;

import K23cnt3.nht.project3.entity.Nhanvien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NhanvienRepository extends JpaRepository<Nhanvien, Integer> {

    List<Nhanvien> findByChucVu(String chucVu);

    List<Nhanvien> findByHoTenContaining(String hoTen);
}