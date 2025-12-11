package K23cnt3.nht._2.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class GioHangDTO {
    private List<GioHangItemDTO> items = new ArrayList<>();
    private BigDecimal tongTien = BigDecimal.ZERO;
    private Integer tongSoLuong = 0;

    public GioHangDTO() {}

    // Tính lại tổng tiền và số lượng
    public void tinhTong() {
        this.tongSoLuong = items.stream()
                .mapToInt(GioHangItemDTO::getSoLuong)
                .sum();

        this.tongTien = items.stream()
                .map(GioHangItemDTO::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Thêm item vào giỏ hàng
    public void addItem(GioHangItemDTO item) {
        // Kiểm tra nếu sản phẩm đã có trong giỏ
        boolean found = false;
        for (GioHangItemDTO existingItem : items) {
            if (existingItem.getMaSP().equals(item.getMaSP())) {
                existingItem.setSoLuong(existingItem.getSoLuong() + item.getSoLuong());
                found = true;
                break;
            }
        }

        // Nếu chưa có thì thêm mới
        if (!found) {
            items.add(item);
        }

        // Tính lại tổng
        tinhTong();
    }

    // Xóa item khỏi giỏ hàng
    public void removeItem(Integer maSP) {
        items.removeIf(item -> item.getMaSP().equals(maSP));
        tinhTong();
    }

    // Cập nhật số lượng
    public void updateQuantity(Integer maSP, Integer soLuong) {
        for (GioHangItemDTO item : items) {
            if (item.getMaSP().equals(maSP)) {
                if (soLuong <= 0) {
                    removeItem(maSP);
                } else {
                    item.setSoLuong(soLuong);
                }
                break;
            }
        }
        tinhTong();
    }

    // Xóa tất cả
    public void clear() {
        items.clear();
        tongTien = BigDecimal.ZERO;
        tongSoLuong = 0;
    }
}

// Inner class cho item trong giỏ hàng
@Data
class GioHangItemDTO {
    private Integer maSP;
    private String tenSP;
    private String hinhAnh;
    private BigDecimal donGia;
    private Integer soLuong;
    private String donViTinh;

    public GioHangItemDTO() {}

    public GioHangItemDTO(Integer maSP, String tenSP, String hinhAnh,
                          BigDecimal donGia, Integer soLuong, String donViTinh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.hinhAnh = hinhAnh;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.donViTinh = donViTinh;
    }

    // Tính thành tiền
    public BigDecimal getThanhTien() {
        if (donGia != null && soLuong != null) {
            return donGia.multiply(BigDecimal.valueOf(soLuong));
        }
        return BigDecimal.ZERO;
    }
}