package K23cnt3.nht._2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Không tìm thấy người dùng");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String email, String type) {
        super("Không tìm thấy " + type + " với email: " + email);
    }

    public UserNotFoundException(Integer id, String type) {
        super("Không tìm thấy " + type + " với mã: " + id);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}