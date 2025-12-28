package NhtK23cnt3.project3.service.cart;

import NhtK23cnt3.project3.entity.product.NhtProduct;
import NhtK23cnt3.project3.model.cart.NhtCartItem;
import NhtK23cnt3.project3.service.product.NhtProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NhtCartService {

    private final NhtProductService productService;

    private static final String CART_SESSION_KEY = "cart";

    /** Lấy Map trong session */
    @SuppressWarnings("unchecked")
    private Map<Long, NhtCartItem> getCartMap(HttpSession session) {
        Object obj = session.getAttribute(CART_SESSION_KEY);
        if (obj == null) {
            Map<Long, NhtCartItem> cart = new LinkedHashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
            return cart;
        }
        return (Map<Long, NhtCartItem>) obj;
    }

    /* ================== API CHÍNH ================== */

    /** Lấy danh sách item trong giỏ (dùng trong controller giỏ hàng) */
    public List<NhtCartItem> getItems(HttpSession session) {
        return new ArrayList<>(getCartMap(session).values());
    }

    /** Thêm 1 sp vào giỏ */
    public void addToCart(Long productId, HttpSession session) {
        Map<Long, NhtCartItem> cart = getCartMap(session);
        NhtCartItem item = cart.get(productId);

        if (item == null) {
            NhtProduct p = productService.getById(productId);
            if (p == null) return;

            item = NhtCartItem.builder()
                    .productId(p.getId())
                    .productName(p.getName())
                    .productImage(p.getImage())
                    .price(p.getPrice())
                    .quantity(1)
                    .build();
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }
        cart.put(productId, item);
    }

    /** Cập nhật số lượng */
    public void updateQuantity(Long productId, int quantity, HttpSession session) {
        Map<Long, NhtCartItem> cart = getCartMap(session);
        NhtCartItem item = cart.get(productId);
        if (item != null) {
            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                item.setQuantity(quantity);
            }
        }
    }

    /** Xoá 1 item */
    public void removeItem(Long productId, HttpSession session) {
        Map<Long, NhtCartItem> cart = getCartMap(session);
        cart.remove(productId);
    }

    /** Tính tổng tiền hiện tại trong giỏ */
    public double getTotal(HttpSession session) {
        return getCartMap(session).values().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    /** Xoá giỏ hàng */
    public void clear(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }


    /** Tên cũ: getCart(session) → trả ra List cho tiện */
    public List<NhtCartItem> getCart(HttpSession session) {
        return getItems(session);
    }

    /** Tên cũ: calculateTotal(items) */
    public double calculateTotal(List<NhtCartItem> items) {
        return items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    /** Tên cũ: clearCart(session) */
    public void clearCart(HttpSession session) {
        clear(session);
    }
}
