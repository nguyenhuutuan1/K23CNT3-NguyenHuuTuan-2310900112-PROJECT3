package K23cnt3.nht._2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import java.util.ArrayList;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    private List<String> errors = new ArrayList<>();

    public ValidationException() {
        super("Dữ liệu không hợp lệ");
    }

    public ValidationException(String message) {
        super(message);
        this.errors.add(message);
    }

    public ValidationException(List<String> errors) {
        super("Có " + errors.size() + " lỗi xác thực");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public String getCombinedErrorMessage() {
        return String.join("; ", errors);
    }
}