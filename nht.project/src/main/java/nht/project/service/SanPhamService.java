package nht.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.enums.TrangThaiSanPham;
import nht.project.model.DanhMuc;
import nht.project.model.SanPham;
import nht.project.repository.DanhMucRepository;
import nht.project.repository.SanPhamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final DanhMucRepository danhMucRepository;

    /**
     * Lấy tất cả sản phẩm
     */
    public List<SanPham> getAllSanPham() {
        log.info("Lấy tất cả sản phẩm");
        return sanPhamRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayTao"));
    }

    /**
     * Lấy sản phẩm có sẵn (còn hàng)
     */
    public List<SanPham> getAvailableSanPham() {
        log.info("Lấy sản phẩm có sẵn");
        return sanPhamRepository.findAllAvailable();
    }

    /**
     * Lấy sản phẩm theo danh mục
     */
    public List<SanPham> getSanPhamByDanhMuc(Long danhMucId) {
        log.info("Lấy sản phẩm theo danh mục ID: {}", danhMucId);
        return sanPhamRepository.findByDanhMucId(danhMucId);
    }

    /**
     * Lấy sản phẩm có sẵn theo danh mục
     */
    public List<SanPham> getAvailableSanPhamByDanhMuc(Long danhMucId) {
        log.info("Lấy sản phẩm có sẵn theo danh mục ID: {}", danhMucId);
        return sanPhamRepository.findAvailableByDanhMucId(danhMucId);
    }

    /**
     * Tìm sản phẩm theo ID
     */
    public Optional<SanPham> getSanPhamById(Long id) {
        log.info("Tìm sản phẩm với ID: {}", id);
        return sanPhamRepository.findById(id);
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa
     */
    public List<SanPham> searchSanPham(String keyword) {
        log.info("Tìm kiếm sản phẩm với từ khóa: {}", keyword);
        return sanPhamRepository.searchByKeyword(keyword);
    }

    /**
     * Tìm kiếm sản phẩm có phân trang
     */
    public Page<SanPham> searchSanPhamWithPaging(String keyword, int page, int size) {
        log.info("Tìm kiếm sản phẩm với phân trang - keyword: {}, page: {}, size: {}", keyword, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));
        return sanPhamRepository.findByTenSanPhamContainingIgnoreCase(keyword, pageable);
    }

    /**
     * Tìm sản phẩm theo khoảng giá
     */
    public List<SanPham> getSanPhamByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Tìm sản phẩm theo khoảng giá: {} - {}", minPrice, maxPrice);
        return sanPhamRepository.findByPriceRangeAndStatus(minPrice, maxPrice, TrangThaiSanPham.AVAILABLE);
    }

    /**
     * Lấy sản phẩm mới nhất
     */
    public List<SanPham> getNewProducts(int limit) {
        log.info("Lấy {} sản phẩm mới nhất", limit);
        return sanPhamRepository.findTop10ByTrangThaiOrderByNgayTaoDesc(TrangThaiSanPham.AVAILABLE);
    }

    /**
     * Lấy sản phẩm bán chạy
     */
    public List<SanPham> getBestSellingProducts(int limit) {
        log.info("Lấy {} sản phẩm bán chạy", limit);
        Pageable pageable = PageRequest.of(0, limit);
        return sanPhamRepository.findBestSellingProducts(pageable);
    }

    /**
     * Lấy sản phẩm sắp hết hàng
     */
    public List<SanPham> getLowStockProducts(int threshold) {
        log.info("Lấy sản phẩm sắp hết hàng (ngưỡng: {})", threshold);
        return sanPhamRepository.findLowStockProducts(threshold);
    }

    /**
     * Lấy sản phẩm hết hàng
     */
    public List<SanPham> getOutOfStockProducts() {
        log.info("Lấy sản phẩm hết hàng");
        return sanPhamRepository.findOutOfStockProducts();
    }

    /**
     * Tạo sản phẩm mới
     */
    @Transactional
    public SanPham createSanPham(SanPham sanPham) {
        log.info("Tạo sản phẩm mới: {}", sanPham.getTenSanPham());

        // Validate
        if (sanPham.getGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0");
        }

        if (sanPham.getSoLuongTon() < 0) {
            throw new IllegalArgumentException("Số lượng tồn không được âm");
        }

        // Kiểm tra danh mục tồn tại
        if (sanPham.getDanhMuc() != null && sanPham.getDanhMuc().getId() != null) {
            DanhMuc danhMuc = danhMucRepository.findById(sanPham.getDanhMuc().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));
            sanPham.setDanhMuc(danhMuc);
        }

        return sanPhamRepository.save(sanPham);
    }

    /**
     * Cập nhật sản phẩm
     */
    @Transactional
    public SanPham updateSanPham(Long id, SanPham sanPhamUpdate) {
        log.info("Cập nhật sản phẩm ID: {}", id);

        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        // Validate
        if (sanPhamUpdate.getGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0");
        }

        if (sanPhamUpdate.getSoLuongTon() < 0) {
            throw new IllegalArgumentException("Số lượng tồn không được âm");
        }

        // Cập nhật thông tin
        sanPham.setTenSanPham(sanPhamUpdate.getTenSanPham());
        sanPham.setMoTa(sanPhamUpdate.getMoTa());
        sanPham.setGia(sanPhamUpdate.getGia());
        sanPham.setSoLuongTon(sanPhamUpdate.getSoLuongTon());
        sanPham.setHinhAnh(sanPhamUpdate.getHinhAnh());
        sanPham.setTrangThai(sanPhamUpdate.getTrangThai());

        // Cập nhật danh mục nếu có
        if (sanPhamUpdate.getDanhMuc() != null && sanPhamUpdate.getDanhMuc().getId() != null) {
            DanhMuc danhMuc = danhMucRepository.findById(sanPhamUpdate.getDanhMuc().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));
            sanPham.setDanhMuc(danhMuc);
        }

        return sanPhamRepository.save(sanPham);
    }

    /**
     * Cập nhật số lượng tồn kho
     */
    @Transactional
    public SanPham updateStock(Long id, int quantity) {
        log.info("Cập nhật tồn kho sản phẩm ID: {}, số lượng: {}", id, quantity);

        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        sanPham.setSoLuongTon(quantity);

        return sanPhamRepository.save(sanPham);
    }

    /**
     * Giảm số lượng tồn kho
     */
    @Transactional
    public void decreaseStock(Long sanPhamId, int quantity) {
        log.info("Giảm tồn kho sản phẩm ID: {}, số lượng: {}", sanPhamId, quantity);

        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        sanPham.giamSoLuong(quantity);
        sanPhamRepository.save(sanPham);
    }

    /**
     * Tăng số lượng tồn kho
     */
    @Transactional
    public void increaseStock(Long sanPhamId, int quantity) {
        log.info("Tăng tồn kho sản phẩm ID: {}, số lượng: {}", sanPhamId, quantity);

        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        sanPham.tangSoLuong(quantity);
        sanPhamRepository.save(sanPham);
    }

    /**
     * Xóa sản phẩm
     */
    @Transactional
    public void deleteSanPham(Long id) {
        log.info("Xóa sản phẩm ID: {}", id);

        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        // Có thể thêm logic kiểm tra sản phẩm đã có trong đơn hàng chưa
        // Nếu có thì không cho xóa hoặc chuyển sang trạng thái DISCONTINUED

        sanPhamRepository.delete(sanPham);
    }

    /**
     * Đếm số sản phẩm theo trạng thái
     */
    public long countByStatus(TrangThaiSanPham trangThai) {
        return sanPhamRepository.countByTrangThai(trangThai);
    }

    /**
     * Đếm số sản phẩm có sẵn
     */
    public long countAvailableProducts() {
        return sanPhamRepository.countAvailableProducts();
    }

    /**
     * Tính tổng giá trị tồn kho
     */
    public BigDecimal calculateTotalInventoryValue() {
        BigDecimal total = sanPhamRepository.calculateTotalInventoryValue();
        return total != null ? total : BigDecimal.ZERO;
    }
}