package K23cnt3.nht._2.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DonHangDTO {
    private Integer maHD;
    private Integer maKH;
    private String tenKH;
    private Integer maNV;
    private String tenNV;
    private LocalDate ngayLapHD;
    private BigDecimal tongTien;
    private String trangThai;
    private String diaChiGiaoHang;
    private String sdtGiaoHang;
    private String ghiChu;

    public DonHangDTO() {}

    public DonHangDTO(Integer maHD, Integer maKH, String tenKH, Integer maNV,
                      String tenNV, LocalDate ngayLapHD, BigDecimal tongTien,
                      String trangThai, String diaChiGiaoHang, String sdtGiaoHang,
                      String ghiChu) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngayLapHD = ngayLapHD;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.sdtGiaoHang = sdtGiaoHang;
        this.ghiChu = ghiChu;
    }
}