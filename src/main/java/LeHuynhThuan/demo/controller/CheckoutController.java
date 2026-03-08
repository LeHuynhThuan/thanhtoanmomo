package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Invoice;
import LeHuynhThuan.demo.entity.User;
import LeHuynhThuan.demo.entity.Voucher;
import LeHuynhThuan.demo.service.BookService;
import LeHuynhThuan.demo.service.CartService;
import LeHuynhThuan.demo.service.InvoiceService;
import LeHuynhThuan.demo.service.MomoPaymentService;
import LeHuynhThuan.demo.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private static final String SESSION_APPLIED_VOUCHER = "appliedVoucher";

    private final CartService cartService;
    private final InvoiceService invoiceService;
    private final VoucherService voucherService;
    private final MomoPaymentService momoPaymentService;
    private final BookService bookService;

    @GetMapping
    public String checkoutForm(Model model, @AuthenticationPrincipal User user, HttpSession session) {
        if (user.getRoles().contains("ADMIN")) {
            return "redirect:/admin";
        }
        if (cartService.getCart().getItems().isEmpty()) {
            return "redirect:/cart";
        }

        double total = cartService.getTotal();
        Voucher appliedVoucher = (Voucher) session.getAttribute(SESSION_APPLIED_VOUCHER);

        // Re-validate voucher khi giỏ hàng thay đổi
        if (appliedVoucher != null) {
            Optional<Voucher> valid = voucherService.findValidByCode(appliedVoucher.getCode(), total);
            if (valid.isEmpty()) {
                session.removeAttribute(SESSION_APPLIED_VOUCHER);
                appliedVoucher = null;
            }
        }

        double discountAmount = 0;
        double finalTotal = total;
        if (appliedVoucher != null) {
            discountAmount = voucherService.calculateDiscount(appliedVoucher, total);
            finalTotal = total - discountAmount;
        }

        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        model.addAttribute("appliedVoucher", appliedVoucher);
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("finalTotal", finalTotal);
        return "checkout/checkout";
    }

    @PostMapping("/apply-voucher")
    public String applyVoucher(@RequestParam String voucherCode,
                              @AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (user.getRoles().contains("ADMIN")) {
            return "redirect:/admin";
        }
        if (cartService.getCart().getItems().isEmpty()) {
            return "redirect:/cart";
        }

        double total = cartService.getTotal();
        Optional<Voucher> voucherOpt = voucherService.findValidByCode(voucherCode, total);

        if (voucherOpt.isPresent()) {
            session.setAttribute(SESSION_APPLIED_VOUCHER, voucherOpt.get());
            redirectAttributes.addFlashAttribute("voucherSuccess", "Áp dụng voucher thành công!");
        } else {
            session.removeAttribute(SESSION_APPLIED_VOUCHER);
            redirectAttributes.addFlashAttribute("voucherError",
                    "Mã voucher không hợp lệ hoặc không đủ điều kiện (kiểm tra đơn tối thiểu và thời hạn).");
        }
        return "redirect:/checkout";
    }

    @PostMapping("/remove-voucher")
    public String removeVoucher(@AuthenticationPrincipal User user, HttpSession session) {
        session.removeAttribute(SESSION_APPLIED_VOUCHER);
        return "redirect:/checkout";
    }

    @PostMapping
    public String processCheckout(@RequestParam String customerName,
                                  @RequestParam String customerEmail,
                                  @RequestParam String customerPhone,
                                  @RequestParam String customerAddress,
                                  @RequestParam(name = "paymentMethod", defaultValue = "cod") String paymentMethod,
                                  @AuthenticationPrincipal User user,
                                  Model model,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        if (user.getRoles().contains("ADMIN")) {
            return "redirect:/admin";
        }
        if (cartService.getCart().getItems().isEmpty()) {
            return "redirect:/cart";
        }

        // Kiểm tra lại tồn kho trước khi tạo đơn
        boolean outOfStock = cartService.getCart().getItems().stream().anyMatch(item ->
                bookService.getBookById(item.getBook().getId())
                        .map(book -> item.getQuantity() > book.getStock())
                        .orElse(true)
        );
        if (outOfStock) {
            redirectAttributes.addFlashAttribute("stockError",
                    "Số lượng sách trong kho không đủ. Vui lòng kiểm tra lại giỏ hàng.");
            return "redirect:/cart";
        }

        Voucher voucher = (Voucher) session.getAttribute(SESSION_APPLIED_VOUCHER);
        double total = cartService.getTotal();

        // Validate voucher lần cuối trước khi đặt hàng
        if (voucher != null) {
            Optional<Voucher> valid = voucherService.findValidByCode(voucher.getCode(), total);
            if (valid.isEmpty()) {
                voucher = null;
            } else {
                voucher = valid.get();
            }
        }
        session.removeAttribute(SESSION_APPLIED_VOUCHER);

        Invoice invoice = new Invoice();
        invoice.setCustomerName(customerName);
        invoice.setCustomerEmail(customerEmail);
        invoice.setCustomerPhone(customerPhone);
        invoice.setCustomerAddress(customerAddress);
        invoice.setUserId(user.getId());
        invoice.setPaymentMethod(paymentMethod);

        Invoice savedInvoice = invoiceService.createInvoice(invoice, cartService.getCart(), voucher);

        if ("momo".equalsIgnoreCase(paymentMethod)) {
            try {
                String payUrl = momoPaymentService.createPayment(savedInvoice);
                cartService.clearCart();
                return "redirect:" + payUrl;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("paymentError",
                        "Không thể kết nối MoMo. Kiểm tra cấu hình (partnerCode, accessKey, secretKey) trong application.properties. Chi tiết: " + e.getMessage());
                return "redirect:/checkout";
            }
        }

        cartService.clearCart();
        model.addAttribute("invoice", savedInvoice);
        model.addAttribute("success", true);
        return "checkout/result";
    }
}
