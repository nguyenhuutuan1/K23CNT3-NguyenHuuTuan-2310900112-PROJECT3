package NhtK23cnt3.project3.model.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NhtCartItem {

    Long productId;
    String productName;
    String productImage;
    Double price;
    Integer quantity;
}
