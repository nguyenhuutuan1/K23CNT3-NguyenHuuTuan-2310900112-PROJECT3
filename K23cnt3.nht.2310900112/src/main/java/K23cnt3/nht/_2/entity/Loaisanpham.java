package K23cnt3.nht._2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "loaisanpham")
@Data
public class Loaisanpham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLoai")
    private Integer maLoai;

    @Column(name = "TenLoai", nullable = false, length = 100)
    private String tenLoai;
}