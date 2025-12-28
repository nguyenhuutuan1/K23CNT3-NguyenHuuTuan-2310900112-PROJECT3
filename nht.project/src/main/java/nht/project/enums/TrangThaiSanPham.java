package nht.project.enums;

public enum TrangThaiSanPham {
    AVAILABLE("Còn hàng"),
    OUT_OF_STOCK("Hết hàng"),
    DISCONTINUED("Ngừng kinh doanh");

    private final String moTa;

    TrangThaiSanPham(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}