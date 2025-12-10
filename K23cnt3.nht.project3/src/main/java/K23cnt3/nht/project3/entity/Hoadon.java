package K23cnt3.nht.project3.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoadon")
@Data
public class Hoadon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHD")
    private Integer maHD;

    @ManyToOne
    @JoinColumn(name = "MaKH")
    private Khachhang khachHang;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private Nhanvien nhanVien;

    @Column(name = "NgayLapHD")
    private LocalDate ngayLapHD;

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "TrangThai", length = 50)
    private String trangThai = "Chờ xử lý";

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chitiethoadon> chiTietHoaDon = new ArrayList<>();
}