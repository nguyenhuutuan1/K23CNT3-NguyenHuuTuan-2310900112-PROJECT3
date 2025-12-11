package K23cnt3.nht._2.enums;

public enum TrangThaiSanPham {
    CON_HANG("Còn hàng"),
    SAP_HET("Sắp hết hàng"),
    HET_HANG("Hết hàng"),
    NGUNG_KINH_DOANH("Ngừng kinh doanh"),
    KHUYEN_MAI("Đang khuyến mãi"),
    NOI_BAT("Nổi bật");

    private final String tenTrangThai;

    TrangThaiSanPham(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    public String getTenTrangThai() {
        return tenTrangThai;
    }

    public String getMaTrangThai() {
        return this.name();
    }

    public static TrangThaiSanPham fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return CON_HANG;
        }

        for (TrangThaiSanPham trangThai : TrangThaiSanPham.values()) {
            if (trangThai.tenTrangThai.equalsIgnoreCase(text) ||
                    trangThai.name().equalsIgnoreCase(text)) {
                return trangThai;
            }
        }

        return CON_HANG;
    }

    // Kiểm tra còn bán không
    public boolean isAvailable() {
        return this == CON_HANG || this == SAP_HET || this == KHUYEN_MAI || this == NOI_BAT;
    }
}