package NhtK23cnt3.project3.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NhtCheckoutForm {

    @NotBlank(message = "Vui lòng nhập họ tên")
    private String customerName;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    private String phone;

    @NotBlank(message = "Vui lòng nhập địa chỉ nhận hàng")
    private String address;

    private String note;

    public String getPaymentMethod() {
        return null;
    }
}
