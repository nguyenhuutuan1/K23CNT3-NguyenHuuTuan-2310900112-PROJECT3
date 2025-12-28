package nht.project.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nht.project.dto.GioHangItem;
import nht.project.model.SanPham;
import nht.project.repository.SanPhamRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GioHangService {

    private final SanPhamRepository sanPhamRepository;
    private static final String CART_SESSION_KEY = "SHOPPING_CART";

    /**
     * Lấy giỏ hàng từ session
     */
    @SuppressWarnings("unchecked")
    public List<GioHangItem> getGioHang(HttpSession session) {
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    public void themSanPham(HttpSession session, Long sanPhamId, Integer soLuong) {
        log.info("Thêm sản phẩm ID: {} với số lượng: {} vào giỏ hàng", sanPhamId, soLuong);

        // Lấy thông tin sản phẩm
        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        // Kiểm tra sản phẩm còn hàng không
        if (!sanPham.isAvailable()) {
            throw new IllegalStateException("Sản phẩm không còn hàng");
        }

        // Kiểm tra số lượng tồn kho
        if (sanPham.getSoLuongTon() < soLuong) {
            throw new IllegalArgumentException("Số lượng vượt quá tồn kho");
        }

        List<GioHangItem> cart = getGioHang(session);

        // Tìm xem sản phẩm đã có trong giỏ chưa
        Optional<GioHangItem> existingItem = cart.stream()
                .filter(item -> item.getSanPhamId().equals(sanPhamId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Nếu đã có, tăng số lượng
            GioHangItem item = existingItem.get();
            int newQuantity = item.getSoLuong() + soLuong;

            if (newQuantity > sanPham.getSoLuongTon()) {
                throw new IllegalArgumentException("Tổng số lượng vượt quá tồn kho");
            }

            item.setSoLuong(newQuantity);
            item.capNhatThanhTien();
        } else {
            // Nếu chưa có, thêm mới
            GioHangItem newItem = new GioHangItem(
                    sanPham.getId(),
                    sanPham.getTenSanPham(),
                    sanPham.getGia(),
                    soLuong,
                    sanPham.getHinhAnh(),
                    sanPham.getSoLuongTon()
            );
            cart.add(newItem);
        }

        session.setAttribute(CART_SESSION_KEY, cart);
        log.info("Giỏ hàng hiện có {} sản phẩm", cart.size());
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    public void capNhatSoLuong(HttpSession session, Long sanPhamId, Integer soLuong) {
        log.info("Cập nhật số lượng sản phẩm ID: {} thành: {}", sanPhamId, soLuong);

        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        List<GioHangItem> cart = getGioHang(session);

        GioHangItem item = cart.stream()
                .filter(i -> i.getSanPhamId().equals(sanPhamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không có trong giỏ hàng"));

        // Kiểm tra tồn kho
        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        if (soLuong > sanPham.getSoLuongTon()) {
            throw new IllegalArgumentException("Số lượng vượt quá tồn kho");
        }

        item.capNhatSoLuong(soLuong);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public void xoaSanPham(HttpSession session, Long sanPhamId) {
        log.info("Xóa sản phẩm ID: {} khỏi giỏ hàng", sanPhamId);

        List<GioHangItem> cart = getGioHang(session);
        cart.removeIf(item -> item.getSanPhamId().equals(sanPhamId));

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void xoaGioHang(HttpSession session) {
        log.info("Xóa toàn bộ giỏ hàng");
        session.removeAttribute(CART_SESSION_KEY);
    }

    /**
     * Tính tổng tiền giỏ hàng
     */
    public BigDecimal tinhTongTien(HttpSession session) {
        List<GioHangItem> cart = getGioHang(session);
        return cart.stream()
                .map(GioHangItem::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Đếm số lượng sản phẩm trong giỏ
     */
    public int demSoLuong(HttpSession session) {
        List<GioHangItem> cart = getGioHang(session);
        return cart.stream()
                .mapToInt(GioHangItem::getSoLuong)
                .sum();
    }

    /**
     * Đếm số mặt hàng khác nhau trong giỏ
     */
    public int demSoMatHang(HttpSession session) {
        return getGioHang(session).size();
    }

    /**
     * Kiểm tra giỏ hàng có trống không
     */
    public boolean isEmpty(HttpSession session) {
        return getGioHang(session).isEmpty();
    }

    /**
     * Kiểm tra tất cả sản phẩm trong giỏ còn đủ hàng không
     */
    public boolean kiemTraTonKho(HttpSession session) {
        List<GioHangItem> cart = getGioHang(session);

        for (GioHangItem item : cart) {
            SanPham sanPham = sanPhamRepository.findById(item.getSanPhamId())
                    .orElse(null);

            if (sanPham == null || !sanPham.isAvailable()) {
                return false;
            }

            if (item.getSoLuong() > sanPham.getSoLuongTon()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Cập nhật thông tin sản phẩm trong giỏ (giá, tồn kho)
     */
    public void capNhatThongTinGioHang(HttpSession session) {
        log.info("Cập nhật thông tin giỏ hàng");

        List<GioHangItem> cart = getGioHang(session);
        List<GioHangItem> updatedCart = new ArrayList<>();

        for (GioHangItem item : cart) {
            Optional<SanPham> sanPhamOpt = sanPhamRepository.findById(item.getSanPhamId());

            if (sanPhamOpt.isPresent()) {
                SanPham sanPham = sanPhamOpt.get();

                // Cập nhật giá và tồn kho
                item.setGia(sanPham.getGia());
                item.setSoLuongTon(sanPham.getSoLuongTon());
                item.setHinhAnh(sanPham.getHinhAnh());

                // Điều chỉnh số lượng nếu vượt tồn kho
                if (item.getSoLuong() > sanPham.getSoLuongTon()) {
                    if (sanPham.getSoLuongTon() > 0) {
                        item.setSoLuong(sanPham.getSoLuongTon());
                    } else {
                        // Bỏ qua sản phẩm hết hàng
                        continue;
                    }
                }

                item.capNhatThanhTien();
                updatedCart.add(item);
            }
        }

        session.setAttribute(CART_SESSION_KEY, updatedCart);
    }
}