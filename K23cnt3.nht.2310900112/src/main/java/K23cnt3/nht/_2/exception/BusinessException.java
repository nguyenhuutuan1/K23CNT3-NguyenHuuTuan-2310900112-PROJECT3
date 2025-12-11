package K23cnt3.nht._2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    private String errorCode;

    public BusinessException() {
        super("Lỗi nghiệp vụ");
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Các lỗi nghiệp vụ phổ biến
    public static BusinessException orderAlreadyProcessed(Integer maHD) {
        return new BusinessException("ORDER_PROCESSED",
                "Đơn hàng #" + maHD + " đã được xử lý, không thể thay đổi");
    }

    public static BusinessException insufficientStock(Integer maSP, String tenSP) {
        return new BusinessException("INSUFFICIENT_STOCK",
                "Sản phẩm " + tenSP + " (mã: " + maSP + ") không đủ số lượng tồn kho");
    }

    public static BusinessException invalidOrderStatus(Integer maHD, String currentStatus, String newStatus) {
        return new BusinessException("INVALID_STATUS",
                "Không thể chuyển trạng thái đơn hàng #" + maHD + " từ '" + currentStatus + "' sang '" + newStatus + "'");
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}