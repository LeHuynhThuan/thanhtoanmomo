package LeHuynhThuan.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private List<Item> items = new ArrayList<>();

    public void addItem(Book book, int quantity) {
        for (Item item : items) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new Item(book, quantity));
    }

    public void removeItem(String bookId) {
        items.removeIf(item -> item.getBook().getId().equals(bookId));
    }

    public void updateQuantity(String bookId, int quantity) {
        for (Item item : items) {
            if (item.getBook().getId().equals(bookId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public double getTotal() {
        return items.stream().mapToDouble(Item::getSubTotal).sum();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public void clear() {
        items.clear();
    }
}
