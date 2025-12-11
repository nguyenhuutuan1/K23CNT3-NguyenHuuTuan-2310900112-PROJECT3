package K23cnt3.nht._2.enums;

public enum TrangThaiDonHang {
    CHO_XU_LY("Chờ xử lý"),
    DA_XAC_NHAN("Đã xác nhận"),
    DANG_CHUAN_BI("Đang chuẩn bị"),
    DANG_GIAO_HANG("Đang giao hàng"),
    DA_GIAO_HANG("Đã giao hàng"),
    DA_THANH_TOAN("Đã thanh toán"),
    DA_HUY("Đã hủy"),
    TRA_HANG("Trả hàng");

    private final String tenTrangThai;

    TrangThaiDonHang(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    public String getTenTrangThai() {
        return tenTrangThai;
    }

    public String getMaTrangThai() {
        return this.name();
    }

    // Chuyển từ string sang enum
    public static TrangThaiDonHang fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return CHO_XU_LY;
        }

        for (TrangThaiDonHang trangThai : TrangThaiDonHang.values()) {
            if (trangThai.tenTrangThai.equalsIgnoreCase(text) ||
                    trangThai.name().equalsIgnoreCase(text)) {
                return trangThai;
            }
        }

        return CHO_XU_LY;
    }

    // Lấy tất cả trạng thái
    public static TrangThaiDonHang[] getAll() {
        return TrangThaiDonHang.values();
    }

    // Kiểm tra có phải trạng thái cuối cùng không
    public boolean isFinalStatus() {
        return this == DA_GIAO_HANG || this == DA_HUY || this == TRA_HANG;
    }

    // Kiểm tra có thể hủy không
    public boolean canCancel() {
        return this == CHO_XU_LY || this == DA_XAC_NHAN || this == DANG_CHUAN_BI;
    }
}