package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Item;
import LeHuynhThuan.demo.service.BookService;
import LeHuynhThuan.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                            @RequestParam(defaultValue = "1") int quantity,
                            RedirectAttributes redirectAttributes) {
        return bookService.getBookById(bookId)
                .map(book -> {
                    // Tìm số lượng hiện có trong giỏ cho sách này
                    int currentInCart = cartService.getCart().getItems().stream()
                            .filter(item -> item.getBook().getId().equals(bookId))
                            .mapToInt(Item::getQuantity)
                            .findFirst()
                            .orElse(0);

                    int requestedTotal = currentInCart + quantity;
                    if (book.getStock() >= 0 && requestedTotal > book.getStock()) {
                        redirectAttributes.addFlashAttribute("error",
                                "Số lượng sách không đủ. Chỉ còn " + book.getStock() + " cuốn trong kho.");
                        return "redirect:/cart";
                    }

                    cartService.addToCart(book, quantity);
                    return "redirect:/cart";
                })
                .orElse("redirect:/cart");
    }

    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable String bookId) {
        cartService.removeFromCart(bookId);
        return "redirect:/cart";
    }

    @PostMapping("/update/{bookId}")
    public String updateQuantity(@PathVariable String bookId,
                                 @RequestParam int quantity,
                                 RedirectAttributes redirectAttributes) {
        return bookService.getBookById(bookId)
                .map(book -> {
                    if (quantity > book.getStock()) {
                        redirectAttributes.addFlashAttribute("error",
                                "Số lượng sách không đủ. Chỉ còn " + book.getStock() + " cuốn trong kho.");
                        return "redirect:/cart";
                    }
                    cartService.updateQuantity(bookId, quantity);
                    return "redirect:/cart";
                })
                .orElse("redirect:/cart");
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
