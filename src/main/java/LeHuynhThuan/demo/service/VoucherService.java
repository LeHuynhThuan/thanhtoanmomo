package LeHuynhThuan.demo.service;

import LeHuynhThuan.demo.entity.Voucher;
import LeHuynhThuan.demo.repository.IVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final IVoucherRepository voucherRepository;

    public List<Voucher> getAll() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getById(String id) {
        return voucherRepository.findById(id);
    }

    public Voucher save(Voucher voucher) {
        // Normalize code
        voucher.setCode(voucher.getCode().trim().toUpperCase());

        // Validate date range
        if (voucher.getStartAt() != null && voucher.getEndAt() != null &&
                voucher.getEndAt().isBefore(voucher.getStartAt())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }

        // Unique code
        voucherRepository.findByCodeIgnoreCase(voucher.getCode())
                .filter(existing -> !existing.getId().equals(voucher.getId()))
                .ifPresent(v -> {
                    throw new IllegalArgumentException("Mã voucher đã tồn tại");
                });

        // Extra validation on discount
        if (voucher.getDiscountType() == Voucher.DiscountType.PERCENT) {
            if (voucher.getDiscountValue() <= 0 || voucher.getDiscountValue() > 100) {
                throw new IllegalArgumentException("Phần trăm giảm phải trong khoảng 0 - 100");
            }
        } else if (voucher.getDiscountType() == Voucher.DiscountType.FIXED) {
            if (voucher.getDiscountValue() <= 0) {
                throw new IllegalArgumentException("Số tiền giảm phải lớn hơn 0");
            }
        }

        return voucherRepository.save(voucher);
    }

    public void delete(String id) {
        voucherRepository.deleteById(id);
    }

    public Optional<Voucher> findValidByCode(String code, double orderTotal) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        String normalized = code.trim().toUpperCase();
        return voucherRepository.findByCodeIgnoreCase(normalized)
                .filter(Voucher::isActive)
                .filter(v -> v.getStartAt() == null || !v.getStartAt().isAfter(LocalDateTime.now()))
                .filter(v -> v.getEndAt() == null || !v.getEndAt().isBefore(LocalDateTime.now()))
                .filter(v -> orderTotal >= v.getMinOrderAmount());
    }

    public double calculateDiscount(Voucher voucher, double orderTotal) {
        if (voucher.getDiscountType() == Voucher.DiscountType.PERCENT) {
            return orderTotal * (voucher.getDiscountValue() / 100.0);
        }
        return Math.min(voucher.getDiscountValue(), orderTotal);
    }
}

