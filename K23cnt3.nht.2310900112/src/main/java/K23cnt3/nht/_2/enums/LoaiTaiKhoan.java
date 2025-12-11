package K23cnt3.nht._2.enums;

public enum LoaiTaiKhoan {
    KHACH_HANG("Khách hàng"),
    NHAN_VIEN("Nhân viên"),
    QUAN_LY("Quản lý"),
    ADMIN("Quản trị viên");

    private final String tenLoai;

    LoaiTaiKhoan(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public String getMaLoai() {
        return this.name();
    }

    // Chuyển từ string sang enum
    public static LoaiTaiKhoan fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return KHACH_HANG;
        }

        for (LoaiTaiKhoan loai : LoaiTaiKhoan.values()) {
            if (loai.tenLoai.equalsIgnoreCase(text) ||
                    loai.name().equalsIgnoreCase(text)) {
                return loai;
            }
        }

        return KHACH_HANG;
    }

    // Lấy tất cả loại tài khoản
    public static LoaiTaiKhoan[] getAll() {
        return LoaiTaiKhoan.values();
    }

    // Kiểm tra có phải admin không
    public boolean isAdmin() {
        return this == ADMIN || this == QUAN_LY;
    }

    // Kiểm tra có phải nhân viên không
    public boolean isEmployee() {
        return this == NHAN_VIEN || this == QUAN_LY || this == ADMIN;
    }

    // Kiểm tra có quyền admin không
    public boolean hasAdminAccess() {
        return this == ADMIN;
    }
}