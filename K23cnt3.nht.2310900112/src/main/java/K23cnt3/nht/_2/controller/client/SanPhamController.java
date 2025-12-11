package K23cnt3.nht._2.controller.client;

import K23cnt3.nht._2.entity.Loaisanpham;
import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.service.LoaisanphamService;
import K23cnt3.nht._2.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/san-pham")
public class SanPhamController {

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private LoaisanphamService loaisanphamService;

    @GetMapping
    public String tatCaSanPham(Model model) {
        List<Sanpham> sanphamList = sanphamService.getAllSanpham();
        List<Loaisanpham> danhMuc = loaisanphamService.getAllLoaisanpham();

        model.addAttribute("sanphamList", sanphamList);
        model.addAttribute("danhMuc", danhMuc);

        return "client/sanpham/list";
    }

    @GetMapping("/{id}")
    public String chiTietSanPham(@PathVariable Integer id, Model model) {
        Sanpham sanpham = sanphamService.getSanphamById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        List<Loaisanpham> danhMuc = loaisanphamService.getAllLoaisanpham();
        List<Sanpham> sanphamCungLoai = sanphamService.getSanphamByLoai(sanpham.getLoaisanpham().getMaLoai());

        model.addAttribute("sanpham", sanpham);
        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("sanphamCungLoai", sanphamCungLoai);

        return "client/sanpham/chitiet";
    }

    @GetMapping("/danh-muc/{maLoai}")
    public String sanPhamTheoDanhMuc(@PathVariable Integer maLoai, Model model) {
        List<Sanpham> sanphamList = sanphamService.getSanphamByLoai(maLoai);
        List<Loaisanpham> danhMuc = loaisanphamService.getAllLoaisanpham();
        Loaisanpham loaiHienTai = loaisanphamService.getLoaisanphamById(maLoai)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        model.addAttribute("sanphamList", sanphamList);
        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("loaiHienTai", loaiHienTai);

        return "client/sanpham/danhmuc";
    }

    @GetMapping("/tim-kiem")
    public String timKiemSanPham(@RequestParam String keyword, Model model) {
        List<Sanpham> ketQua = sanphamService.searchSanpham(keyword);
        List<Loaisanpham> danhMuc = loaisanphamService.getAllLoaisanpham();

        model.addAttribute("ketQua", ketQua);
        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("keyword", keyword);

        return "client/sanpham/timkiem";
    }
}