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
    private List<ItemInvoice> items = new ArrayList<>();
    private double totalAmount;
    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private OrderStatus status = OrderStatus.PENDING;
}
