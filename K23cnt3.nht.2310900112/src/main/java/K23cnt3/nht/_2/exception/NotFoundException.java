package K23cnt3.nht._2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public NotFoundException() {
        super("Không tìm thấy tài nguyên");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s không tồn tại với %s: %s",
                resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}