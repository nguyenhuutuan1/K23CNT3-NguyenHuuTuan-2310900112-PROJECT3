package nht.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "khach_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "mat_khau", length = 255)
    private String matKhau;

    @Column(name = "role", length = 20)
    private String role = "CUSTOMER"; // CUSTOMER hoặc ADMIN

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonHang> danhSachDonHang = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    // Helper methods
    public String getThongTinLienHe() {
        StringBuilder info = new StringBuilder();
        if (hoTen != null) info.append(hoTen);
        if (soDienThoai != null) info.append(" - ").append(soDienThoai);
        if (email != null) info.append(" - ").append(email);
        return info.toString();
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}