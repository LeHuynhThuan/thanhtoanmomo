package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Voucher;
import LeHuynhThuan.demo.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/vouchers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class VoucherAdminController {

    private final VoucherService voucherService;

    @GetMapping
    public String listVouchers(Model model) {
        model.addAttribute("vouchers", voucherService.getAll());
        model.addAttribute("voucher", new Voucher());
        return "admin/vouchers";
    }

    @GetMapping("/edit/{id}")
    public String editVoucher(@PathVariable String id, Model model) {
        return voucherService.getById(id)
                .map(v -> {
                    model.addAttribute("vouchers", voucherService.getAll());
                    model.addAttribute("voucher", v);
                    return "admin/vouchers";
                })
                .orElse("redirect:/admin/vouchers");
    }

    // Trường hợp truy cập nhầm /admin/vouchers/edit/ (không có id) thì quay lại danh sách
    @GetMapping("/edit")
    public String editVoucherWithoutId() {
        return "redirect:/admin/vouchers";
    }

    @PostMapping("/save")
    public String saveVoucher(@Valid @ModelAttribute("voucher") Voucher voucher,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vouchers", voucherService.getAll());
            return "admin/vouchers";
        }

        try {
            voucherService.save(voucher);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("vouchers", voucherService.getAll());
            model.addAttribute("error", ex.getMessage());
            return "admin/vouchers";
        }

        return "redirect:/admin/vouchers";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable String id) {
        voucherService.delete(id);
        return "redirect:/admin/vouchers";
    }
}

