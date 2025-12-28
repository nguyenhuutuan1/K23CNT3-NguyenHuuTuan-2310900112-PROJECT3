package nht.project.enums;

public enum PhuongThucThanhToan {
    TIEN_MAT("Tiền mặt khi nhận hàng"),
    CHUYEN_KHOAN("Chuyển khoản ngân hàng");

    private final String moTa;

    PhuongThucThanhToan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}