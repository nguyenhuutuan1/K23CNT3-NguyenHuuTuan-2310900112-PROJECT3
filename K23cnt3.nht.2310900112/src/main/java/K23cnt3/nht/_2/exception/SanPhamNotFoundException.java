package K23cnt3.nht._2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SanPhamNotFoundException extends RuntimeException {

    public SanPhamNotFoundException() {
        super("Không tìm thấy sản phẩm");
    }

    public SanPhamNotFoundException(String message) {
        super(message);
    }

    public SanPhamNotFoundException(Integer maSP) {
        super("Không tìm thấy sản phẩm với mã: " + maSP);
    }

    public SanPhamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}