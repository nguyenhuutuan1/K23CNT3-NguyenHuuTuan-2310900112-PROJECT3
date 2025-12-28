package NhtK23cnt3.project3.entity.order;

public enum NhtOrderStatus {

    WAIT_CONFIRM("Chờ xác nhận"),
    PREPARING("Đang chuẩn bị"),
    SHIPPING("Đang giao"),
    COMPLETED("Hoàn tất"),
    CANCELLED("Đã hủy");

    private final String viLabel;

    NhtOrderStatus(String viLabel) {
        this.viLabel = viLabel;
    }

    public String getViLabel() {
        return viLabel;
    }
}
