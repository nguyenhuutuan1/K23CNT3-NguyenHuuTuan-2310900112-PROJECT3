package nht.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nht.project.enums.PhuongThucThanhToan;
import nht.project.enums.TrangThaiThanhToan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "thanh_toan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "don_hang_id", nullable = false)
    private DonHang donHang;

    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc", nullable = false, length = 50)
    private PhuongThucThanhToan phuongThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private TrangThaiThanhToan trangThai = TrangThaiThanhToan.DA_THANH_TOAN;

    @Column(name = "ma_giao_dich", length = 100)
    private String maGiaoDich;

    @Column(name = "ngay_thanh_toan")
    private LocalDateTime ngayThanhToan;

    @PrePersist
    protected void onCreate() {
        if (ngayThanhToan == null) {
            ngayThanhToan = LocalDateTime.now();
        }
        if (trangThai == null) {
            trangThai = TrangThaiThanhToan.DA_THANH_TOAN;
        }
        if (maGiaoDich == null || maGiaoDich.isEmpty()) {
            maGiaoDich = generateMaGiaoDich();
        }
    }

    // Helper methods
    private String generateMaGiaoDich() {
        // Format: TT + YYYYMMDDHHMMSS + random 4 digits
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        int random = (int) (Math.random() * 10000);
        return String.format("TT%s%04d", timestamp, random);
    }

    public boolean isThanhCong() {
        return trangThai == TrangThaiThanhToan.DA_THANH_TOAN;
    }

    public String getThongTinThanhToan() {
        StringBuilder info = new StringBuilder();
        info.append("Mã GD: ").append(maGiaoDich);
        info.append(" | Số tiền: ").append(String.format("%,.0f", soTien)).append(" VNĐ");
        info.append(" | Phương thức: ").append(phuongThuc.getMoTa());
        info.append(" | Trạng thái: ").append(trangThai.getMoTa());
        return info.toString();
    }
}