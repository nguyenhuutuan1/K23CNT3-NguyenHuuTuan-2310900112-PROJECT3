package K23cnt3.nht._2.enums;

public enum PhuongThucThanhToan {
    TIEN_MAT("Tiền mặt khi nhận hàng"),
    CHUYEN_KHOAN("Chuyển khoản ngân hàng"),
    VI_DIEN_TU("Ví điện tử (Momo, ZaloPay)"),
    THE_NOI_DIA("Thẻ ATM nội địa"),
    THE_QUOC_TE("Thẻ quốc tế (Visa/Mastercard)"),
    TRA_SAU("Trả sau (COD)"),
    TAI_CUA_HANG("Tại cửa hàng");

    private final String tenPhuongThuc;

    PhuongThucThanhToan(String tenPhuongThuc) {
        this.tenPhuongThuc = tenPhuongThuc;
    }

    public String getTenPhuongThuc() {
        return tenPhuongThuc;
    }

    public String getMaPhuongThuc() {
        return this.name();
    }

    // Chuyển từ string sang enum
    public static PhuongThucThanhToan fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return TIEN_MAT;
        }

        for (PhuongThucThanhToan phuongThuc : PhuongThucThanhToan.values()) {
            if (phuongThuc.tenPhuongThuc.equalsIgnoreCase(text) ||
                    phuongThuc.name().equalsIgnoreCase(text)) {
                return phuongThuc;
            }
        }

        return TIEN_MAT;
    }

    // Lấy tất cả phương thức thanh toán
    public static PhuongThucThanhToan[] getAll() {
        return PhuongThucThanhToan.values();
    }

    // Kiểm tra có phải thanh toán online không
    public boolean isOnlinePayment() {
        return this == CHUYEN_KHOAN || this == VI_DIEN_TU ||
                this == THE_NOI_DIA || this == THE_QUOC_TE;
    }
}