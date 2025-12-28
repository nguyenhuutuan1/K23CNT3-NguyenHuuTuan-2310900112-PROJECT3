package NhtK23cnt3.project3.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NhtOrderTrackForm {

    @NotNull(message = "Vui lòng nhập mã đơn hàng")
    private Long orderId;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    private String phone;
}
