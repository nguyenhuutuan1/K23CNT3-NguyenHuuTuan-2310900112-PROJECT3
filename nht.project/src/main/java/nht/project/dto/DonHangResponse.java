package nht.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nht.project.enums.PhuongThucThanhToan;
import nht.project.enums.TrangThaiDonHang;
import nht.project.enums.TrangThaiThanhToan;
import nht.project.model.DonHang;
import nht.project.model.ChiTietDonHang;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHangResponse {

    private Long id;
    private String maDonHang;
    private String hoTenKhachHang;
    private String soDienThoaiKhachHang;
    private String emailKhachHang;
    private BigDecimal tongTien;
    private PhuongThucThanhToan phuongThucThanhToan;
    private TrangThaiThanhToan trangThaiThanhToan;
    private TrangThaiDonHang trangThaiDonHang;
    private String diaChiGiaoHang;
    private String ghiChu;
    private LocalDateTime ngayDat;
    private LocalDateTime ngayCapNhat;
    private List<ChiTietResponse> chiTietList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietResponse {
        private Long sanPhamId;
        private String tenSanPham;
        private Integer soLuong;
        private BigDecimal giaBan;
        private BigDecimal thanhTien;
        private String hinhAnh;

        public String getFormattedGiaBan() {
            return String.format("%,.0f VNĐ", giaBan);
        }

        public String getFormattedThanhTien() {
            return String.format("%,.0f VNĐ", thanhTien);
        }
    }

    // Constructor từ Entity
    public static DonHangResponse fromEntity(DonHang donHang) {
        DonHangResponse response = new DonHangResponse();
        response.setId(donHang.getId());
        response.setMaDonHang(donHang.getMaDonHang());

        if (donHang.getKhachHang() != null) {
            response.setHoTenKhachHang(donHang.getKhachHang().getHoTen());
            response.setSoDienThoaiKhachHang(donHang.getKhachHang().getSoDienThoai());
            response.setEmailKhachHang(donHang.getKhachHang().getEmail());
        }

        response.setTongTien(donHang.getTongTien());
        response.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        response.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        response.setTrangThaiDonHang(donHang.getTrangThaiDonHang());
        response.setDiaChiGiaoHang(donHang.getDiaChiGiaoHang());
        response.setGhiChu(donHang.getGhiChu());
        response.setNgayDat(donHang.getNgayDat());
        response.setNgayCapNhat(donHang.getNgayCapNhat());

        // Convert chi tiết
        if (donHang.getChiTietDonHangs() != null) {
            response.setChiTietList(
                    donHang.getChiTietDonHangs().stream()
                            .map(ct -> new ChiTietResponse(
                                    ct.getSanPham().getId(),
                                    ct.getSanPham().getTenSanPham(),
                                    ct.getSoLuong(),
                                    ct.getGiaBan(),
                                    ct.getThanhTien(),
                                    ct.getSanPham().getHinhAnh()
                            ))
                            .collect(Collectors.toList())
            );
        } else {
            response.setChiTietList(new ArrayList<>());
        }

        return response;
    }

    // Formatted methods
    public String getFormattedTongTien() {
        return String.format("%,.0f VNĐ", tongTien);
    }

    public String getFormattedNgayDat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return ngayDat.format(formatter);
    }

    public String getFormattedNgayCapNhat() {
        if (ngayCapNhat != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return ngayCapNhat.format(formatter);
        }
        return "";
    }

    public String getTrangThaiDonHangText() {
        return trangThaiDonHang != null ? trangThaiDonHang.getMoTa() : "";
    }

    public String getTrangThaiThanhToanText() {
        return trangThaiThanhToan != null ? trangThaiThanhToan.getMoTa() : "";
    }

    public String getPhuongThucThanhToanText() {
        return phuongThucThanhToan != null ? phuongThucThanhToan.getMoTa() : "";
    }

    public int getTongSoLuongSanPham() {
        return chiTietList.stream()
                .mapToInt(ChiTietResponse::getSoLuong)
                .sum();
    }

    public int getSoMatHang() {
        return chiTietList.size();
    }
}