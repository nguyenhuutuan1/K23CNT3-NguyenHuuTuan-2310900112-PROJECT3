package nht.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nht.project.enums.PhuongThucThanhToan;
import nht.project.enums.TrangThaiDonHang;
import nht.project.enums.TrangThaiThanhToan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "don_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_don_hang", unique = true, nullable = false, length = 50)
    private String maDonHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @Column(name = "tong_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc_thanh_toan", nullable = false, length = 50)
    private PhuongThucThanhToan phuongThucThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_thanh_toan", length = 50)
    private TrangThaiThanhToan trangThaiThanhToan = TrangThaiThanhToan.CHUA_THANH_TOAN;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_don_hang", length = 50)
    private TrangThaiDonHang trangThaiDonHang = TrangThaiDonHang.CHO_XU_LY;

    @Column(name = "dia_chi_giao_hang", columnDefinition = "TEXT")
    private String diaChiGiaoHang;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "ngay_dat", updatable = false)
    private LocalDateTime ngayDat;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ThanhToan> danhSachThanhToan = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        ngayDat = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
        if (maDonHang == null || maDonHang.isEmpty()) {
            maDonHang = generateMaDonHang();
        }
        if (trangThaiThanhToan == null) {
            trangThaiThanhToan = TrangThaiThanhToan.CHUA_THANH_TOAN;
        }
        if (trangThaiDonHang == null) {
            trangThaiDonHang = TrangThaiDonHang.CHO_XU_LY;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    // Helper methods
    private String generateMaDonHang() {
        // Format: DH + YYYYMMDDHHMMSS + random 3 digits
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        int random = (int) (Math.random() * 1000);
        return String.format("DH%s%03d", timestamp, random);
    }

    public void themChiTiet(ChiTietDonHang chiTiet) {
        chiTietDonHangs.add(chiTiet);
        chiTiet.setDonHang(this);
    }

    public void xoaChiTiet(ChiTietDonHang chiTiet) {
        chiTietDonHangs.remove(chiTiet);
        chiTiet.setDonHang(null);
    }

    public BigDecimal tinhTongTien() {
        return chiTietDonHangs.stream()
                .map(ChiTietDonHang::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean coTheHuy() {
        return trangThaiDonHang == TrangThaiDonHang.CHO_XU_LY;
    }

    public boolean coTheThanhToan() {
        return trangThaiThanhToan == TrangThaiThanhToan.CHUA_THANH_TOAN
                && trangThaiDonHang != TrangThaiDonHang.HUY;
    }
}