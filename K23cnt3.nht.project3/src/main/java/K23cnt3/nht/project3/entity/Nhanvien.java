package K23cnt3.nht.project3.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "nhanvien")
@Data
public class Nhanvien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNV")
    private Integer maNV;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;

    @Column(name = "GioiTinh", length = 10)
    private String gioiTinh;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "DienThoai", length = 20)
    private String dienThoai;

    @Column(name = "Email", length = 50)
    private String email;

    @Column(name = "ChucVu", length = 50)
    private String chucVu;

    @Column(name = "Luong", precision = 18, scale = 2)
    private BigDecimal luong;
}
