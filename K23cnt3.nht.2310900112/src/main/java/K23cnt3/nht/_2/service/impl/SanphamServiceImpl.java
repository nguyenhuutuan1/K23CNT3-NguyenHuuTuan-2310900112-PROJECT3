package K23cnt3.nht._2.service.impl;

import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.repository.SanphamRepository;
import K23cnt3.nht._2.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SanphamServiceImpl implements SanphamService {

    @Autowired
    private SanphamRepository sanphamRepository;

    @Override
    public List<Sanpham> getAllSanpham() {
        return sanphamRepository.findAll();
    }

    @Override
    public Optional<Sanpham> getSanphamById(Integer id) {
        return sanphamRepository.findById(id);
    }

    @Override
    public Sanpham saveSanpham(Sanpham sanpham) {
        return sanphamRepository.save(sanpham);
    }

    @Override
    public void deleteSanpham(Integer id) {
        sanphamRepository.deleteById(id);
    }

    @Override
    public List<Sanpham> searchSanpham(String keyword) {
        return sanphamRepository.timKiemSanPham(keyword);
    }

    @Override
    public List<Sanpham> getSanphamByLoai(Integer maLoai) {
        return sanphamRepository.findByLoaisanpham_MaLoai(maLoai);
    }

    @Override
    public List<Sanpham> getSanphamConHang() {
        return sanphamRepository.findSanPhamConHang();
    }

    @Override
    public List<Sanpham> getSanphamNoiBat() {
        // Lấy 8 sản phẩm còn hàng đầu tiên làm sản phẩm nổi bật
        List<Sanpham> sanphamConHang = sanphamRepository.findSanPhamConHang();
        return sanphamConHang.stream().limit(8).toList();
    }
}