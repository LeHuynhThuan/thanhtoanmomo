package LeHuynhThuan.demo.service;

import LeHuynhThuan.demo.entity.Book;
import LeHuynhThuan.demo.entity.Cart;
import LeHuynhThuan.demo.entity.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class CartService {
    private final Cart cart = new Cart();

    public Cart getCart() {
        return cart;
    }

    public void addToCart(Book book, int quantity) {
        cart.addItem(book, quantity);
    }

    public void removeFromCart(String bookId) {
        cart.removeItem(bookId);
    }

    public void updateQuantity(String bookId, int quantity) {
        if (quantity <= 0) {
            cart.removeItem(bookId);
        } else {
            cart.updateQuantity(bookId, quantity);
        }
    }

    public double getTotal() {
        return cart.getTotal();
    }

    public int getTotalQuantity() {
        return cart.getTotalQuantity();
    }

    public void clearCart() {
        cart.clear();
    }
}
