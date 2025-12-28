package nht.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nht.project.enums.PhuongThucThanhToan;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHangRequest {

    // Thông tin khách hàng
    private Long khachHangId;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChiGiaoHang;

    // Thông tin đơn hàng
    private PhuongThucThanhToan phuongThucThanhToan;
    private String ghiChu;

    // Danh sách sản phẩm (nếu không dùng giỏ hàng từ session)
    private List<ItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRequest {
        private Long sanPhamId;
        private Integer soLuong;
    }

    // Validation methods
    public boolean isValid() {
        return hoTen != null && !hoTen.trim().isEmpty()
                && soDienThoai != null && !soDienThoai.trim().isEmpty()
                && diaChiGiaoHang != null && !diaChiGiaoHang.trim().isEmpty()
                && phuongThucThanhToan != null;
    }

    public String getValidationMessage() {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            return "Vui lòng nhập họ tên";
        }
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            return "Vui lòng nhập số điện thoại";
        }
        if (!isValidPhoneNumber(soDienThoai)) {
            return "Số điện thoại không hợp lệ";
        }
        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            return "Email không hợp lệ";
        }
        if (diaChiGiaoHang == null || diaChiGiaoHang.trim().isEmpty()) {
            return "Vui lòng nhập địa chỉ giao hàng";
        }
        if (phuongThucThanhToan == null) {
            return "Vui lòng chọn phương thức thanh toán";
        }
        return null;
    }

    private boolean isValidPhoneNumber(String phone) {
        // Kiểm tra số điện thoại Việt Nam (10 số, bắt đầu bằng 0)
        return phone.matches("^0\\d{9}$");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}