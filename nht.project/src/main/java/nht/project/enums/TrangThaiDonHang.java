package nht.project.enums;

public enum TrangThaiDonHang {
    CHO_XU_LY("Chờ xử lý"),
    DANG_GIAO("Đang giao hàng"),
    HOAN_THANH("Hoàn thành"),
    HUY("Đã hủy");

    private final String moTa;

    TrangThaiDonHang(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}