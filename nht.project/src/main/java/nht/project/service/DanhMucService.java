package nht.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.model.DanhMuc;
import nht.project.repository.DanhMucRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DanhMucService {

    private final DanhMucRepository danhMucRepository;

    /**
     * Lấy tất cả danh mục
     */
    public List<DanhMuc> getAllDanhMuc() {
        log.info("Lấy tất cả danh mục");
        return danhMucRepository.findAllByOrderByTenDanhMucAsc();
    }

    /**
     * Lấy danh mục có sản phẩm available
     */
    public List<DanhMuc> getDanhMucWithAvailableProducts() {
        log.info("Lấy danh mục có sản phẩm available");
        return danhMucRepository.findAllWithAvailableProducts();
    }

    /**
     * Tìm danh mục theo ID
     */
    public Optional<DanhMuc> getDanhMucById(Long id) {
        log.info("Tìm danh mục với ID: {}", id);
        return danhMucRepository.findById(id);
    }

    /**
     * Tìm danh mục theo tên
     */
    public Optional<DanhMuc> getDanhMucByTen(String tenDanhMuc) {
        log.info("Tìm danh mục theo tên: {}", tenDanhMuc);
        return danhMucRepository.findByTenDanhMuc(tenDanhMuc);
    }

    /**
     * Tìm kiếm danh mục theo từ khóa
     */
    public List<DanhMuc> searchDanhMuc(String keyword) {
        log.info("Tìm kiếm danh mục với từ khóa: {}", keyword);
        return danhMucRepository.findByTenDanhMucContainingIgnoreCase(keyword);
    }

    /**
     * Tạo danh mục mới
     */
    @Transactional
    public DanhMuc createDanhMuc(DanhMuc danhMuc) {
        log.info("Tạo danh mục mới: {}", danhMuc.getTenDanhMuc());

        // Kiểm tra trùng tên
        if (danhMucRepository.existsByTenDanhMuc(danhMuc.getTenDanhMuc())) {
            throw new IllegalArgumentException("Danh mục đã tồn tại");
        }

        return danhMucRepository.save(danhMuc);
    }

    /**
     * Cập nhật danh mục
     */
    @Transactional
    public DanhMuc updateDanhMuc(Long id, DanhMuc danhMucUpdate) {
        log.info("Cập nhật danh mục ID: {}", id);

        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

        // Kiểm tra trùng tên (nếu đổi tên)
        if (!danhMuc.getTenDanhMuc().equals(danhMucUpdate.getTenDanhMuc())) {
            if (danhMucRepository.existsByTenDanhMuc(danhMucUpdate.getTenDanhMuc())) {
                throw new IllegalArgumentException("Tên danh mục đã tồn tại");
            }
        }

        danhMuc.setTenDanhMuc(danhMucUpdate.getTenDanhMuc());
        danhMuc.setMoTa(danhMucUpdate.getMoTa());

        return danhMucRepository.save(danhMuc);
    }

    /**
     * Xóa danh mục
     */
    @Transactional
    public void deleteDanhMuc(Long id) {
        log.info("Xóa danh mục ID: {}", id);

        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

        // Kiểm tra còn sản phẩm không
        long productCount = danhMucRepository.countProductsByDanhMucId(id);
        if (productCount > 0) {
            throw new IllegalStateException("Không thể xóa danh mục còn sản phẩm");
        }

        danhMucRepository.delete(danhMuc);
    }

    /**
     * Đếm số sản phẩm trong danh mục
     */
    public long countProductsInDanhMuc(Long danhMucId) {
        return danhMucRepository.countProductsByDanhMucId(danhMucId);
    }
}