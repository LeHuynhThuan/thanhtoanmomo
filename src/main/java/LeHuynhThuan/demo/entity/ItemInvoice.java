package LeHuynhThuan.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInvoice {
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private int quantity;
    private double price;
    private double subTotal;
}
