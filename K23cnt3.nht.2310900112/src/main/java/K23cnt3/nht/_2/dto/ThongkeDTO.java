package K23cnt3.nht._2.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ThongkeDTO {
    private LocalDate ngay;
    private Integer soHoaDon;
    private BigDecimal tongDoanhThu;
    private Integer tongSanPhamBan;

    public ThongkeDTO() {}

    public ThongkeDTO(LocalDate ngay, Integer soHoaDon, BigDecimal tongDoanhThu, Integer tongSanPhamBan) {
        this.ngay = ngay;
        this.soHoaDon = soHoaDon;
        this.tongDoanhThu = tongDoanhThu;
        this.tongSanPhamBan = tongSanPhamBan;
    }
}