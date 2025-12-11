package K23cnt3.nht._2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nhacungcap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nhacungcap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNCC")
    private Integer maNCC;

    @Column(name = "TenNCC", nullable = false, length = 100)
    private String tenNCC;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "DienThoai", length = 20)
    private String dienThoai;

    @Column(name = "Email", length = 50)
    private String email;
}