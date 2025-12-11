package K23cnt3.nht._2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "hoadon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hoadon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHD")
    private Integer maHD;

    @ManyToOne
    @JoinColumn(name = "MaKH")
    private Khachhang khachhang;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private Nhanvien nhanvien;

    @Column(name = "NgayLapHD")
    private LocalDate ngayLapHD;

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;
}