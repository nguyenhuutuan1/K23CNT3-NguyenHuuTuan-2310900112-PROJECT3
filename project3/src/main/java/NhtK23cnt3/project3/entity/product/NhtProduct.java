package NhtK23cnt3.project3.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class NhtProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // đã ALTER thành BIGINT

    @Column(nullable = false, length = 255)
    private String name;

    // chỉ lưu tên file ảnh
    @Column(length = 500)
    private String image;

    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private NhtCategory category;

    @Column(name = "tag")
    private String tag;

}
