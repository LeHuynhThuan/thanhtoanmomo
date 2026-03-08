package LeHuynhThuan.demo.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vouchers")
public class Voucher {

    public enum DiscountType {
        PERCENT,
        FIXED
    }

    @Id
    private String id;

    @NotBlank(message = "Mã voucher không được để trống")
    private String code;

    private String description;

    @NotNull(message = "Loại giảm giá là bắt buộc")
    private DiscountType discountType;

    @Positive(message = "Giá trị giảm phải lớn hơn 0")
    private double discountValue;

    @Min(value = 0, message = "Đơn tối thiểu không được âm")
    private double minOrderAmount;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private boolean active = true;

    /**
     * Số lượng voucher còn lại.
     * null nghĩa là không giới hạn số lần sử dụng.
     */
    private Integer remainingQuantity;
}

