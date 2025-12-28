package nht.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeResponse {

    // Thống kê tổng quan
    private Long tongSoSanPham;
    private Long sanPhamConHang;
    private Long sanPhamHetHang;
    private BigDecimal giaTriTonKho;

    private Long tongSoDonHang;
    private Long donHangChoXuLy;
    private Long donHangDangGiao;
    private Long donHangHoanThanh;
    private Long donHangDaHuy;

    private Long tongSoKhachHang;
    private BigDecimal tongDoanhThu;
    private BigDecimal doanhThuTienMat;
    private BigDecimal doanhThuChuyenKhoan;

    // Thống kê chi tiết
    private Long donHangChuaThanhToan;
    private Long donHangDaThanhToan;

    // Constructor đơn giản cho thống kê cơ bản
    public ThongKeResponse(Long tongSoSanPham, Long sanPhamConHang, Long tongSoDonHang,
                           Long tongSoKhachHang, BigDecimal tongDoanhThu) {
        this.tongSoSanPham = tongSoSanPham;
        this.sanPhamConHang = sanPhamConHang;
        this.tongSoDonHang = tongSoDonHang;
        this.tongSoKhachHang = tongSoKhachHang;
        this.tongDoanhThu = tongDoanhThu;
    }

    // Formatted methods
    public String getFormattedGiaTriTonKho() {
        return giaTriTonKho != null ? String.format("%,.0f VNĐ", giaTriTonKho) : "0 VNĐ";
    }

    public String getFormattedTongDoanhThu() {
        return tongDoanhThu != null ? String.format("%,.0f VNĐ", tongDoanhThu) : "0 VNĐ";
    }

    public String getFormattedDoanhThuTienMat() {
        return doanhThuTienMat != null ? String.format("%,.0f VNĐ", doanhThuTienMat) : "0 VNĐ";
    }

    public String getFormattedDoanhThuChuyenKhoan() {
        return doanhThuChuyenKhoan != null ? String.format("%,.0f VNĐ", doanhThuChuyenKhoan) : "0 VNĐ";
    }

    // Tính phần trăm
    public double getPhanTramSanPhamConHang() {
        if (tongSoSanPham == null || tongSoSanPham == 0) return 0;
        return (sanPhamConHang != null ? sanPhamConHang : 0) * 100.0 / tongSoSanPham;
    }

    public double getPhanTramDonHangHoanThanh() {
        if (tongSoDonHang == null || tongSoDonHang == 0) return 0;
        return (donHangHoanThanh != null ? donHangHoanThanh : 0) * 100.0 / tongSoDonHang;
    }

    public double getPhanTramThanhToan() {
        if (tongSoDonHang == null || tongSoDonHang == 0) return 0;
        return (donHangDaThanhToan != null ? donHangDaThanhToan : 0) * 100.0 / tongSoDonHang;
    }
}