package K23cnt3.nht._2.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaiKhoanDTO {
    private Integer maKH;
    private String hoTen;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String dienThoai;
    private String email;
    private String taiKhoan;
    private String matKhau;
    private String xacNhanMatKhau;
    private Integer diemTichLuy = 0;

    public TaiKhoanDTO() {}

    // Constructor cho đăng ký
    public TaiKhoanDTO(String hoTen, String email, String dienThoai,
                       String diaChi, String taiKhoan, String matKhau) {
        this.hoTen = hoTen;
        this.email = email;
        this.dienThoai = dienThoai;
        this.diaChi = diaChi;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
    }

    // Constructor cho cập nhật thông tin
    public TaiKhoanDTO(Integer maKH, String hoTen, LocalDate ngaySinh,
                       String gioiTinh, String diaChi, String dienThoai,
                       String email) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.dienThoai = dienThoai;
        this.email = email;
    }

    // Kiểm tra password match (cho đăng ký)
    public boolean isPasswordMatch() {
        return matKhau != null && matKhau.equals(xacNhanMatKhau);
    }
}