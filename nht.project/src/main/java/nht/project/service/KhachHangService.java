package nht.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.model.KhachHang;
import nht.project.repository.KhachHangRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;

    /**
     * Lấy tất cả khách hàng
     */
    public List<KhachHang> getAllKhachHang() {
        log.info("Lấy tất cả khách hàng");
        return khachHangRepository.findAll();
    }

    /**
     * Tìm khách hàng theo ID
     */
    public Optional<KhachHang> getKhachHangById(Long id) {
        log.info("Tìm khách hàng với ID: {}", id);
        return khachHangRepository.findById(id);
    }

    /**
     * Tìm khách hàng theo email
     */
    public Optional<KhachHang> getKhachHangByEmail(String email) {
        log.info("Tìm khách hàng theo email: {}", email);
        return khachHangRepository.findByEmail(email);
    }

    /**
     * Tìm khách hàng theo số điện thoại
     */
    public Optional<KhachHang> getKhachHangBySoDienThoai(String soDienThoai) {
        log.info("Tìm khách hàng theo SĐT: {}", soDienThoai);
        return khachHangRepository.findBySoDienThoai(soDienThoai);
    }

    /**
     * Tìm kiếm khách hàng theo từ khóa
     */
    public List<KhachHang> searchKhachHang(String keyword) {
        log.info("Tìm kiếm khách hàng với từ khóa: {}", keyword);
        return khachHangRepository.searchByKeyword(keyword);
    }

    /**
     * Đăng ký khách hàng mới
     */
    @Transactional
    public KhachHang registerKhachHang(KhachHang khachHang) {
        log.info("Đăng ký khách hàng mới: {}", khachHang.getEmail());

        // Validate email
        if (khachHang.getEmail() != null && !khachHang.getEmail().trim().isEmpty()) {
            if (khachHangRepository.existsByEmail(khachHang.getEmail())) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }
        }

        // Validate số điện thoại
        if (khachHang.getSoDienThoai() != null && !khachHang.getSoDienThoai().trim().isEmpty()) {
            if (khachHangRepository.existsBySoDienThoai(khachHang.getSoDienThoai())) {
                throw new IllegalArgumentException("Số điện thoại đã được sử dụng");
            }
        }

        // TODO: Hash password nếu có (có thể dùng BCryptPasswordEncoder)
        // Hiện tại để đơn giản, lưu trực tiếp

        return khachHangRepository.save(khachHang);
    }

    /**
     * Đăng nhập
     */
    public Optional<KhachHang> login(String email, String matKhau) {
        log.info("Đăng nhập với email: {}", email);

        // TODO: So sánh password đã hash
        // Hiện tại đơn giản so sánh trực tiếp
        return khachHangRepository.findByEmailAndMatKhau(email, matKhau);
    }

    /**
     * Tạo khách hàng mới (không cần đăng ký - dùng cho khách vãng lai)
     */
    @Transactional
    public KhachHang createKhachHang(String hoTen, String soDienThoai, String email, String diaChi) {
        log.info("Tạo khách hàng vãng lai: {}", hoTen);

        // Kiểm tra khách hàng đã tồn tại chưa
        if (email != null && !email.trim().isEmpty()) {
            Optional<KhachHang> existing = khachHangRepository.findByEmail(email);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        if (soDienThoai != null && !soDienThoai.trim().isEmpty()) {
            Optional<KhachHang> existing = khachHangRepository.findBySoDienThoai(soDienThoai);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(hoTen);
        khachHang.setSoDienThoai(soDienThoai);
        khachHang.setEmail(email);
        khachHang.setDiaChi(diaChi);

        return khachHangRepository.save(khachHang);
    }

    /**
     * Cập nhật thông tin khách hàng
     */
    @Transactional
    public KhachHang updateKhachHang(Long id, KhachHang khachHangUpdate) {
        log.info("Cập nhật khách hàng ID: {}", id);

        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng"));

        // Validate email (nếu thay đổi)
        if (khachHangUpdate.getEmail() != null &&
                !khachHangUpdate.getEmail().equals(khachHang.getEmail())) {
            if (khachHangRepository.existsByEmail(khachHangUpdate.getEmail())) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }
        }

        // Validate số điện thoại (nếu thay đổi)
        if (khachHangUpdate.getSoDienThoai() != null &&
                !khachHangUpdate.getSoDienThoai().equals(khachHang.getSoDienThoai())) {
            if (khachHangRepository.existsBySoDienThoai(khachHangUpdate.getSoDienThoai())) {
                throw new IllegalArgumentException("Số điện thoại đã được sử dụng");
            }
        }

        khachHang.setHoTen(khachHangUpdate.getHoTen());
        khachHang.setSoDienThoai(khachHangUpdate.getSoDienThoai());
        khachHang.setEmail(khachHangUpdate.getEmail());
        khachHang.setDiaChi(khachHangUpdate.getDiaChi());

        return khachHangRepository.save(khachHang);
    }

    /**
     * Đổi mật khẩu
     */
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Đổi mật khẩu cho khách hàng ID: {}", id);

        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng"));

        // TODO: Verify old password với hash
        if (!khachHang.getMatKhau().equals(oldPassword)) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }

        // TODO: Hash new password
        khachHang.setMatKhau(newPassword);
        khachHangRepository.save(khachHang);
    }

    /**
     * Xóa khách hàng
     */
    @Transactional
    public void deleteKhachHang(Long id) {
        log.info("Xóa khách hàng ID: {}", id);

        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng"));

        // Kiểm tra khách hàng có đơn hàng không
        long orderCount = khachHangRepository.countOrdersByKhachHangId(id);
        if (orderCount > 0) {
            throw new IllegalStateException("Không thể xóa khách hàng đã có đơn hàng");
        }

        khachHangRepository.delete(khachHang);
    }

    /**
     * Lấy khách hàng VIP (nhiều đơn nhất)
     */
    public List<KhachHang> getTopCustomers() {
        log.info("Lấy danh sách khách hàng VIP");
        return khachHangRepository.findTopCustomers();
    }

    /**
     * Lấy khách hàng mới
     */
    public List<KhachHang> getNewCustomers() {
        log.info("Lấy danh sách khách hàng mới");
        return khachHangRepository.findTop10ByOrderByNgayTaoDesc();
    }

    /**
     * Đếm số đơn hàng của khách hàng
     */
    public long countOrders(Long khachHangId) {
        return khachHangRepository.countOrdersByKhachHangId(khachHangId);
    }
}