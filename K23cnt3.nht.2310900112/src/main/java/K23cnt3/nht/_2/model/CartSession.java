package K23cnt3.nht._2.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class CartSession {
    private Map<Integer, CartItem> items = new HashMap<>();
    private BigDecimal tongTien = BigDecimal.ZERO;
    private Integer tongSoLuong = 0;

    public CartSession() {}

    // Thêm sản phẩm vào giỏ
    public void addItem(Integer maSP, String tenSP, String hinhAnh,
                        BigDecimal donGia, Integer soLuong, String donViTinh) {
        if (items.containsKey(maSP)) {
            CartItem existingItem = items.get(maSP);
            existingItem.setSoLuong(existingItem.getSoLuong() + soLuong);
        } else {
            CartItem newItem = new CartItem(maSP, tenSP, hinhAnh, donGia, soLuong, donViTinh);
            items.put(maSP, newItem);
        }
        tinhTong();
    }

    // Cập nhật số lượng
    public void updateQuantity(Integer maSP, Integer soLuong) {
        if (items.containsKey(maSP)) {
            if (soLuong <= 0) {
                items.remove(maSP);
            } else {
                items.get(maSP).setSoLuong(soLuong);
            }
            tinhTong();
        }
    }

    // Xóa sản phẩm
    public void removeItem(Integer maSP) {
        items.remove(maSP);
        tinhTong();
    }

    // Xóa tất cả
    public void clear() {
        items.clear();
        tongTien = BigDecimal.ZERO;
        tongSoLuong = 0;
    }

    // Tính tổng tiền và số lượng
    private void tinhTong() {
        tongSoLuong = items.values().stream()
                .mapToInt(CartItem::getSoLuong)
                .sum();

        tongTien = items.values().stream()
                .map(CartItem::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Kiểm tra giỏ hàng trống
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Lấy số lượng sản phẩm trong giỏ
    public int getItemCount() {
        return items.size();
    }
}

// Inner class cho item trong session
@Data
class CartItem {
    private Integer maSP;
    private String tenSP;
    private String hinhAnh;
    private BigDecimal donGia;
    private Integer soLuong;
    private String donViTinh;

    public CartItem() {}

    public CartItem(Integer maSP, String tenSP, String hinhAnh,
                    BigDecimal donGia, Integer soLuong, String donViTinh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.hinhAnh = hinhAnh;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.donViTinh = donViTinh;
    }

    public BigDecimal getThanhTien() {
        if (donGia != null && soLuong != null) {
            return donGia.multiply(BigDecimal.valueOf(soLuong));
        }
        return BigDecimal.ZERO;
    }
}