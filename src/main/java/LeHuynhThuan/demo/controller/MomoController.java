package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Invoice;
import LeHuynhThuan.demo.entity.OrderStatus;
import LeHuynhThuan.demo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MomoController {

    private final InvoiceService invoiceService;

    /**
     * Người dùng sẽ được MoMo redirect về URL này sau khi thanh toán.
     */
    @GetMapping("/momo/return")
    public String handleMomoReturn(@RequestParam("orderId") String orderId,
                                   @RequestParam("resultCode") int resultCode,
                                   @RequestParam(value = "message", required = false) String message,
                                   Model model) {
        Optional<Invoice> invoiceOpt = invoiceService.getInvoiceById(orderId);
        if (invoiceOpt.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Không tìm thấy đơn hàng tương ứng.");
            return "checkout/result";
        }

        Invoice invoice = invoiceOpt.get();

        if (resultCode == 0) {
            invoice.setStatus(OrderStatus.PROCESSING);
            invoiceService.saveInvoice(invoice);
            model.addAttribute("success", true);
        } else {
            invoice.setStatus(OrderStatus.CANCELLED);
            invoiceService.saveInvoice(invoice);
            model.addAttribute("success", false);
            String error = (message != null && !message.isBlank())
                    ? message
                    : "Thanh toán không thành công hoặc đã bị hủy. (Mã: " + resultCode + ")";
            model.addAttribute("message", error);
        }

        model.addAttribute("invoice", invoice);
        return "checkout/result";
    }

    /**
     * Webhook IPN từ MoMo để xác nhận trạng thái thanh toán.
     * Ở đây chỉ cập nhật trạng thái đơn hàng đơn giản.
     */
    @PostMapping("/momo/ipn")
    public ResponseEntity<Map<String, Object>> handleMomoIpn(@RequestParam("orderId") String orderId,
                                                             @RequestParam("resultCode") int resultCode) {
        Optional<Invoice> invoiceOpt = invoiceService.getInvoiceById(orderId);
        invoiceOpt.ifPresent(invoice -> {
            if (resultCode == 0) {
                invoice.setStatus(OrderStatus.PROCESSING);
            } else {
                invoice.setStatus(OrderStatus.CANCELLED);
            }
            invoiceService.saveInvoice(invoice);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("resultCode", 0);
        response.put("message", "OK");
        return ResponseEntity.ok(response);
    }
}

