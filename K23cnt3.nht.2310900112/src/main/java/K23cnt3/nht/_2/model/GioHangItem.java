package K23cnt3.nht._2.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GioHangItem {
    private Integer maSP;
    private String tenSP;
    private String hinhAnh;
    private BigDecimal donGia;
    private Integer soLuong;
    private String donViTinh;

    public GioHangItem() {}

    public GioHangItem(Integer maSP, String tenSP, String hinhAnh,
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

    // Kiểm tra hợp lệ
    public boolean isValid() {
        return maSP != null && tenSP != null && soLuong != null && soLuong > 0;
    }
}