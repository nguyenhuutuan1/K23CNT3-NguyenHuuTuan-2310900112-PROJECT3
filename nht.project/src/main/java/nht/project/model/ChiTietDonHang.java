package nht.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_don_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "don_hang_id", nullable = false)
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "gia_ban", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "thanh_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal thanhTien;

    @PrePersist
    @PreUpdate
    protected void calculateThanhTien() {
        if (soLuong != null && giaBan != null) {
            this.thanhTien = giaBan.multiply(BigDecimal.valueOf(soLuong));
        }
    }

    // Constructor tiện ích
    public ChiTietDonHang(DonHang donHang, SanPham sanPham, Integer soLuong, BigDecimal giaBan) {
        this.donHang = donHang;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        calculateThanhTien();
    }

    // Helper methods
    public void capNhatSoLuong(Integer soLuongMoi) {
        this.soLuong = soLuongMoi;
        calculateThanhTien();
    }

    public void tangSoLuong(Integer soLuongThem) {
        this.soLuong += soLuongThem;
        calculateThanhTien();
    }

    public void giamSoLuong(Integer soLuongGiam) {
        if (this.soLuong >= soLuongGiam) {
            this.soLuong -= soLuongGiam;
            calculateThanhTien();
        } else {
            throw new IllegalArgumentException("Số lượng giảm vượt quá số lượng hiện tại");
        }
    }
}