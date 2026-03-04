package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Invoice;
import LeHuynhThuan.demo.entity.User;
import LeHuynhThuan.demo.service.CartService;
import LeHuynhThuan.demo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CartService cartService;
    private final InvoiceService invoiceService;

    @GetMapping
    public String checkoutForm(Model model, @AuthenticationPrincipal User user) {
        // Prevent admin from accessing checkout
        if (user.getRoles().contains("ADMIN")) {
            return "redirect:/admin";
        }
        
        if (cartService.getCart().getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("user", user);
        return "checkout/checkout";
    }

    @PostMapping
    public String processCheckout(@RequestParam String customerName,
                                   @RequestParam String customerEmail,
                                   @RequestParam String customerPhone,
                                   @RequestParam String customerAddress,
                                   @AuthenticationPrincipal User user,
                                   Model model) {
        // Prevent admin from processing checkout
        if (user.getRoles().contains("ADMIN")) {
            return "redirect:/admin";
        }
        
        Invoice invoice = new Invoice();
        invoice.setCustomerName(customerName);
        invoice.setCustomerEmail(customerEmail);
        invoice.setCustomerPhone(customerPhone);
        invoice.setCustomerAddress(customerAddress);
        invoice.setUserId(user.getId());

        Invoice savedInvoice = invoiceService.createInvoice(invoice, cartService.getCart());
        cartService.clearCart();

        model.addAttribute("invoice", savedInvoice);
        return "checkout/result";
    }
}
