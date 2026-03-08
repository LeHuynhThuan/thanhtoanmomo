package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Invoice;
import LeHuynhThuan.demo.entity.OrderStatus;
import LeHuynhThuan.demo.entity.User;
import LeHuynhThuan.demo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final InvoiceService invoiceService;

    @GetMapping
    public String userOrders(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(user.getId());
        model.addAttribute("orders", invoices);
        model.addAttribute("OrderStatus", OrderStatus.class);
        return "orders/history";
    }

    @GetMapping("/{id}")
    public String userOrderDetail(@PathVariable String id,
                                  @AuthenticationPrincipal User user,
                                  Model model) {
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Admin có thể xem chi tiết mọi đơn hàng
        boolean isAdmin = user.getRoles() != null && user.getRoles().contains("ADMIN");

        return invoiceService.getInvoiceById(id)
                .filter(inv -> isAdmin || user.getId().equals(inv.getUserId()))
                .map(inv -> {
                    model.addAttribute("order", inv);
                    model.addAttribute("OrderStatus", OrderStatus.class);
                    return "orders/detail";
                })
                .orElse("redirect:/orders");
    }
}

