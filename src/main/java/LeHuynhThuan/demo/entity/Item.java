package LeHuynhThuan.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Book book;
    private int quantity;

    public double getSubTotal() {
        return book.getPrice() * quantity;
    }
}
