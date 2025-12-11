package K23cnt3.nht._2.controller.client;

import K23cnt3.nht._2.entity.Loaisanpham;
import K23cnt3.nht._2.entity.Sanpham;
import K23cnt3.nht._2.service.LoaisanphamService;
import K23cnt3.nht._2.service.SanphamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private SanphamService sanphamService;

    @Autowired
    private LoaisanphamService loaisanphamService;

    @GetMapping("/")
    public String home(Model model) {
        List<Sanpham> sanphamNoiBat = sanphamService.getSanphamNoiBat();
        List<Sanpham> sanphamConHang = sanphamService.getSanphamConHang();
        List<Loaisanpham> danhMuc = loaisanphamService.getAllLoaisanpham();

        model.addAttribute("sanphamNoiBat", sanphamNoiBat);
        model.addAttribute("sanphamConHang", sanphamConHang);
        model.addAttribute("danhMuc", danhMuc);

        return "client/index";
    }

    @GetMapping("/gioi-thieu")
    public String gioiThieu() {
        return "client/gioithieu";
    }

    @GetMapping("/lien-he")
    public String lienHe() {
        return "client/lienhe";
    }
}