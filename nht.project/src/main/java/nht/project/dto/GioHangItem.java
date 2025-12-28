package nht.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GioHangItem {

    private Long sanPhamId;
    private String tenSanPham;
    private BigDecimal gia;
    private Integer soLuong;
    private BigDecimal thanhTien;
    private String hinhAnh;
    private Integer soLuongTon; // Để kiểm tra tồn kho

    // Constructor tiện ích
    public GioHangItem(Long sanPhamId, String tenSanPham, BigDecimal gia, Integer soLuong, String hinhAnh, Integer soLuongTon) {
        this.sanPhamId = sanPhamId;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
        this.soLuongTon = soLuongTon;
        this.thanhTien = tinhThanhTien();
    }

    // Tính thành tiền
    public BigDecimal tinhThanhTien() {
        if (gia != null && soLuong != null) {
            return gia.multiply(BigDecimal.valueOf(soLuong));
        }
        return BigDecimal.ZERO;
    }

    // Cập nhật thành tiền
    public void capNhatThanhTien() {
        this.thanhTien = tinhThanhTien();
    }

    // Tăng số lượng
    public void tangSoLuong(Integer soLuongThem) {
        if (this.soLuong + soLuongThem <= soLuongTon) {
            this.soLuong += soLuongThem;
            capNhatThanhTien();
        } else {
            throw new IllegalArgumentException("Vượt quá số lượng tồn kho");
        }
    }

    // Giảm số lượng
    public void giamSoLuong(Integer soLuongGiam) {
        if (this.soLuong - soLuongGiam >= 1) {
            this.soLuong -= soLuongGiam;
            capNhatThanhTien();
        } else {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
    }

    // Cập nhật số lượng
    public void capNhatSoLuong(Integer soLuongMoi) {
        if (soLuongMoi <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        if (soLuongMoi > soLuongTon) {
            throw new IllegalArgumentException("Vượt quá số lượng tồn kho");
        }
        this.soLuong = soLuongMoi;
        capNhatThanhTien();
    }

    // Kiểm tra còn đủ hàng không
    public boolean kiemTraTonKho() {
        return soLuong <= soLuongTon;
    }
}