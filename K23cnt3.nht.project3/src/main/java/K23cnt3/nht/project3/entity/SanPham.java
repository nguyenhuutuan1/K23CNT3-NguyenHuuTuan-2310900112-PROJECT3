package K23cnt3.nht.project3.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "sanpham")
@Data
public class Sanpham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP", nullable = false, length = 150)
    private String tenSP;

    @ManyToOne
    @JoinColumn(name = "MaLoai")
    private Loaisanpham loaiSanPham;

    @ManyToOne
    @JoinColumn(name = "MaNCC")
    private Nhacungcap nhaCungCap;

    @Column(name = "DonViTinh", length = 20)
    private String donViTinh;

    @Column(name = "DonGia", precision = 18, scale = 2)
    private BigDecimal donGia;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Column(name = "MoTa", columnDefinition = "TEXT")
    private String moTa;
}
