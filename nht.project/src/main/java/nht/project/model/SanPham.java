package nht.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nht.project.enums.TrangThaiSanPham;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_san_pham", nullable = false, length = 200)
    private String tenSanPham;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "gia", nullable = false, precision = 15, scale = 2)
    private BigDecimal gia;

    @Column(name = "so_luong_ton", nullable = false)
    private Integer soLuongTon = 0;

    @Column(name = "hinh_anh", length = 500)
    private String hinhAnh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_id")
    private DanhMuc danhMuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private TrangThaiSanPham trangThai = TrangThaiSanPham.AVAILABLE;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
        if (trangThai == null) {
            trangThai = TrangThaiSanPham.AVAILABLE;
        }
        if (soLuongTon == null) {
            soLuongTon = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
        // Tự động cập nhật trạng thái dựa trên số lượng tồn
        if (soLuongTon <= 0) {
            trangThai = TrangThaiSanPham.OUT_OF_STOCK;
        } else if (trangThai == TrangThaiSanPham.OUT_OF_STOCK && soLuongTon > 0) {
            trangThai = TrangThaiSanPham.AVAILABLE;
        }
    }

    // Helper methods
    public boolean isAvailable() {
        return trangThai == TrangThaiSanPham.AVAILABLE && soLuongTon > 0;
    }

    public void giamSoLuong(int soLuong) {
        if (this.soLuongTon >= soLuong) {
            this.soLuongTon -= soLuong;
        } else {
            throw new IllegalArgumentException("Không đủ số lượng tồn kho");
        }
    }

    public void tangSoLuong(int soLuong) {
        this.soLuongTon += soLuong;
    }
}