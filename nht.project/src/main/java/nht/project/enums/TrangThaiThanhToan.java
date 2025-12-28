package nht.project.enums;

public enum TrangThaiThanhToan {
    CHUA_THANH_TOAN("Chưa thanh toán"),
    DA_THANH_TOAN("Đã thanh toán"),
    THAT_BAI("Thất bại"),
    HOAN_TIEN("Hoàn tiền");

    private final String moTa;

    TrangThaiThanhToan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}