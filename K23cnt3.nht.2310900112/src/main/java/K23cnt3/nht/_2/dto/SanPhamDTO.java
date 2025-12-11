package K23cnt3.nht._2.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SanPhamDTO {
    private Integer maSP;
    private String tenSP;
    private Integer maLoai;
    private String tenLoai;
    private Integer maNCC;
    private String tenNCC;
    private String donViTinh;
    private BigDecimal donGia;
    private Integer soLuong;
    private String hinhAnh;
    private String moTa;
    private Boolean noiBat;

    // Constructor mặc định
    public SanPhamDTO() {}

    // Constructor đầy đủ
    public SanPhamDTO(Integer maSP, String tenSP, Integer maLoai, String tenLoai,
                      Integer maNCC, String tenNCC, String donViTinh,
                      BigDecimal donGia, Integer soLuong, String hinhAnh,
                      String moTa, Boolean noiBat) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.noiBat = noiBat;
    }
}