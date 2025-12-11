package K23cnt3.nht._2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "chitiethoadon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chitiethoadon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTHD")
    private Integer maCTHD;

    @ManyToOne
    @JoinColumn(name = "MaHD")
    private Hoadon hoadon;

    @ManyToOne
    @JoinColumn(name = "MaSP")
    private Sanpham sanpham;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGia", precision = 18, scale = 2)
    private BigDecimal donGia;

    @Column(name = "ThanhTien", precision = 18, scale = 2)
    private BigDecimal thanhTien;
}