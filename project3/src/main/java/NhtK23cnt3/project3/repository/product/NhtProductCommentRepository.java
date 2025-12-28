package NhtK23cnt3.project3.repository.product;

import NhtK23cnt3.project3.entity.product.NhtProductComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NhtProductCommentRepository
        extends JpaRepository<NhtProductComment, Long> {

    List<NhtProductComment>
    findByProductIdOrderByCreatedAtDesc(Long productId);
}
