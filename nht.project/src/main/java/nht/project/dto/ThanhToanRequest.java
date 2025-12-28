package nht.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nht.project.enums.PhuongThucThanhToan;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToanRequest {

    private Long donHangId;
    private String maDonHang;
    private BigDecimal soTien;
    private PhuongThucThanhToan phuongThuc;
    private String maGiaoDich; // Dùng cho chuyển khoản

    // Thông tin chuyển khoản (nếu có)
    private String nganHang;
    private String soTaiKhoan;
    private String tenTaiKhoan;

    // Validation
    public boolean isValid() {
        if (donHangId == null && (maDonHang == null || maDonHang.trim().isEmpty())) {
            return false;
        }
        if (soTien == null || soTien.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (phuongThuc == null) {
            return false;
        }

        // Nếu là chuyển khoản, cần có mã giao dịch
        if (phuongThuc == PhuongThucThanhToan.CHUYEN_KHOAN) {
            return maGiaoDich != null && !maGiaoDich.trim().isEmpty();
        }

        return true;
    }

    public String getValidationMessage() {
        if (donHangId == null && (maDonHang == null || maDonHang.trim().isEmpty())) {
            return "Không tìm thấy thông tin đơn hàng";
        }
        if (soTien == null || soTien.compareTo(BigDecimal.ZERO) <= 0) {
            return "Số tiền thanh toán không hợp lệ";
        }
        if (phuongThuc == null) {
            return "Vui lòng chọn phương thức thanh toán";
        }
        if (phuongThuc == PhuongThucThanhToan.CHUYEN_KHOAN
                && (maGiaoDich == null || maGiaoDich.trim().isEmpty())) {
            return "Vui lòng nhập mã giao dịch chuyển khoản";
        }
        return null;
    }
}