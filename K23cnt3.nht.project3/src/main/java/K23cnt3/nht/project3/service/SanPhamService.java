package K23cnt3.nht.project3.service;

import K23cnt3.nht.project3.entity.Sanpham;
import K23cnt3.nht.project3.repository.SanphamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SanphamService {

    @Autowired
    private SanphamRepository sanphamRepository;

    public List<Sanpham> getAllSanpham() {
        return sanphamRepository.findAll();
    }

    public Sanpham getSanphamById(Integer id) {
        Optional<Sanpham> optional = sanphamRepository.findById(id);
        return optional.orElse(null);
    }

    public Sanpham saveSanpham(Sanpham sanpham) {
        return sanphamRepository.save(sanpham);
    }

    public void deleteSanpham(Integer id) {
        sanphamRepository.deleteById(id);
    }

    public List<Sanpham> searchSanpham(String keyword) {
        return sanphamRepository.findByTenSPContaining(keyword);
    }

    public List<Sanpham> getSanphamByLoai(Integer maLoai) {
        return sanphamRepository.findByLoaiSanPham_MaLoai(maLoai);
    }

    public List<Sanpham> getSanphamConHang() {
        return sanphamRepository.findBySoLuongGreaterThan(0);
    }

    // Thêm method này nếu chưa có
    public List<Sanpham> getSanphamHetHang() {
        return sanphamRepository.findAll().stream()
                .filter(sp -> sp.getSoLuong() <= 0)
                .toList();
    }
}