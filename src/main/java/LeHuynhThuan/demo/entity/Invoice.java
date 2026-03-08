package LeHuynhThuan.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private String userId;
    private String paymentMethod;           // Hình thức thanh toán (COD, MoMo, ...)
    private List<ItemInvoice> items = new ArrayList<>();
    private double totalAmount;           // Tổng tiền trước giảm giá
    private String voucherCode;           // Mã voucher đã áp dụng (nếu có)
    private double discountAmount;         // Số tiền giảm giá
    private double finalAmount;            // Tổng tiền sau giảm giá (= totalAmount - discountAmount)
    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private OrderStatus status = OrderStatus.PENDING;

    /** Số tiền cuối cùng hiển thị (tương thích đơn cũ chưa có finalAmount) */
    public double getDisplayFinalAmount() {
        return finalAmount > 0 ? finalAmount : totalAmount;
    }
}
