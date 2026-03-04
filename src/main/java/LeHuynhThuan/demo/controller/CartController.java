package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.service.BookService;
import LeHuynhThuan.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final BookService bookService;

    @GetMapping
    public String viewCart(Model model) {
        // Remove items whose books have been deleted
        cartService.getCart().getItems()
                .removeIf(item -> bookService.getBookById(item.getBook().getId()).isEmpty());

        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("total", cartService.getTotal());
        return "cart/cart";
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable String bookId,
                             @RequestParam(defaultValue = "1") int quantity) {
        bookService.getBookById(bookId).ifPresent(book -> {
            cartService.addToCart(book, quantity);
        });
        return "redirect:/cart";
    }

    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable String bookId) {
        cartService.removeFromCart(bookId);
        return "redirect:/cart";
    }

    @PostMapping("/update/{bookId}")
    public String updateQuantity(@PathVariable String bookId,
                                  @RequestParam int quantity) {
        cartService.updateQuantity(bookId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
