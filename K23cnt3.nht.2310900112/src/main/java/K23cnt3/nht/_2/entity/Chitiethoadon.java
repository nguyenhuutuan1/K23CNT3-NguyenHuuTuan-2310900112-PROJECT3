package K23cnt3.nht._2.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "chitiethoadon")
@Data
public class Chitiethoadon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTHD")
    private Integer maCTHD;

    @ManyToOne
    @JoinColumn(name = "MaHD")
    private Hoadon hoaDon;

    @ManyToOne
    @JoinColumn(name = "MaSP")
    private Sanpham sanPham;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGia", precision = 18, scale = 2)
    private BigDecimal donGia;

    @Column(name = "ThanhTien", precision = 18, scale = 2)
    private BigDecimal thanhTien;

    // ĐỔI TỪ PRIVATE SANG PUBLIC
    public void calculateThanhTien() {
        if (donGia != null && soLuong != null) {
            thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
        }
    }
}