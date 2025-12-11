package K23cnt3.nht._2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CartException extends RuntimeException {

    public CartException() {
        super("Lỗi giỏ hàng");
    }

    public CartException(String message) {
        super(message);
    }

    public CartException(Integer maSP) {
        super("Lỗi với sản phẩm mã: " + maSP);
    }

    public CartException(Integer maSP, String reason) {
        super("Lỗi với sản phẩm mã " + maSP + ": " + reason);
    }

    // Các lỗi cụ thể cho giỏ hàng
    public static CartException productNotFound(Integer maSP) {
        return new CartException("Sản phẩm mã " + maSP + " không tồn tại");
    }

    public static CartException outOfStock(Integer maSP, Integer soLuong, Integer tonKho) {
        return new CartException("Sản phẩm mã " + maSP + " chỉ còn " + tonKho + " sản phẩm. " +
                "Bạn đang yêu cầu " + soLuong + " sản phẩm.");
    }

    public static CartException invalidQuantity(Integer maSP) {
        return new CartException("Số lượng sản phẩm mã " + maSP + " không hợp lệ");
    }

    public static CartException emptyCart() {
        return new CartException("Giỏ hàng trống");
    }

    public CartException(String message, Throwable cause) {
        super(message, cause);
    }
}