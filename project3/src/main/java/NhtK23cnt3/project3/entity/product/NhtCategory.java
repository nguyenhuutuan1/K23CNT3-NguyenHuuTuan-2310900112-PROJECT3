package NhtK23cnt3.project3.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
public class NhtCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // dùng Long để đồng bộ với service & repo

    @Column(nullable = false, length = 255)
    private String name;
}
