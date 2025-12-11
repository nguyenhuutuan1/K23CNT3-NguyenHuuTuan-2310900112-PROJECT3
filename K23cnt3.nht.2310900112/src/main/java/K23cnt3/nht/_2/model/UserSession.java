package K23cnt3.nht._2.model;

import lombok.Data;

@Data
public class UserSession {
    private Integer maKH;
    private String hoTen;
    private String email;
    private String dienThoai;
    private boolean isLoggedIn;
    private boolean isAdmin;

    public UserSession() {
        this.isLoggedIn = false;
        this.isAdmin = false;
    }

    public UserSession(Integer maKH, String hoTen, String email, String dienThoai) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.email = email;
        this.dienThoai = dienThoai;
        this.isLoggedIn = true;
        this.isAdmin = false;
    }

    // Tạo session admin
    public static UserSession createAdminSession(Integer maNV, String hoTen, String email) {
        UserSession session = new UserSession();
        session.maKH = maNV; // Dùng maKH field để lưu maNV
        session.hoTen = hoTen;
        session.email = email;
        session.isLoggedIn = true;
        session.isAdmin = true;
        return session;
    }

    // Clear session
    public void clear() {
        this.maKH = null;
        this.hoTen = null;
        this.email = null;
        this.dienThoai = null;
        this.isLoggedIn = false;
        this.isAdmin = false;
    }
}