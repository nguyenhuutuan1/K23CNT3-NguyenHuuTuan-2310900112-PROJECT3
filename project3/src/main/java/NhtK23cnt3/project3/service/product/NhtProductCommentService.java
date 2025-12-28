package NhtK23cnt3.project3.service.product;

import NhtK23cnt3.project3.entity.product.NhtProductComment;
import NhtK23cnt3.project3.repository.product.NhtProductCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NhtProductCommentService {

    private final NhtProductCommentRepository commentRepository;

    public List<NhtProductComment> getByProduct(Long productId) {
        return commentRepository
                .findByProductIdOrderByCreatedAtDesc(productId);
    }

    public void save(NhtProductComment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
