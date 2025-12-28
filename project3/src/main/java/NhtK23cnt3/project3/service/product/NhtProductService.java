package NhtK23cnt3.project3.service.product;

import NhtK23cnt3.project3.entity.product.NhtProduct;
import NhtK23cnt3.project3.repository.product.NhtProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhtProductService {

    private final NhtProductRepository productRepository;

    public List<NhtProduct> getAll() {
        return productRepository.findAll();
    }

    public List<NhtProduct> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public NhtProduct getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public List<NhtProduct> searchByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<NhtProduct> findByTag(String tag) {
        return productRepository.findByTag(tag);
    }
}
