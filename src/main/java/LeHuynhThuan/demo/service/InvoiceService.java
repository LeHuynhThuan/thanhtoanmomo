package LeHuynhThuan.demo.service;

import LeHuynhThuan.demo.entity.*;
import LeHuynhThuan.demo.repository.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final IInvoiceRepository invoiceRepository;
    private final VoucherService voucherService;
    private final BookService bookService;

    public Invoice createInvoice(Invoice invoice, Cart cart) {
        return createInvoice(invoice, cart, null);
    }

    public Invoice createInvoice(Invoice invoice, Cart cart, Voucher voucher) {
        List<ItemInvoice> itemInvoices = cart.getItems().stream()
                .map(item -> {
                    ItemInvoice ii = new ItemInvoice();
                    ii.setBookId(item.getBook().getId());
                    ii.setBookTitle(item.getBook().getTitle());
                    ii.setBookAuthor(item.getBook().getAuthor());
                    ii.setQuantity(item.getQuantity());
                    ii.setPrice(item.getBook().getPrice());
                    ii.setSubTotal(item.getSubTotal());
                    return ii;
                })
                .collect(Collectors.toList());

        invoice.setItems(itemInvoices);
        double subtotal = cart.getTotal();
        invoice.setTotalAmount(subtotal);

        double discountAmount = 0;
        if (voucher != null) {
            discountAmount = voucherService.calculateDiscount(voucher, subtotal);
            invoice.setVoucherCode(voucher.getCode());
            invoice.setDiscountAmount(discountAmount);

            // Mỗi lần dùng voucher thì trừ 1 đơn vị số lượng (nếu có giới hạn)
            if (voucher.getRemainingQuantity() != null && voucher.getRemainingQuantity() > 0) {
                voucher.setRemainingQuantity(voucher.getRemainingQuantity() - 1);
                voucherService.save(voucher);
            }
        }
        invoice.setFinalAmount(subtotal - discountAmount);

        invoice.setOrderDate(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setStatus(OrderStatus.PENDING);
        Invoice saved = invoiceRepository.save(invoice);

        // Sau khi tạo hóa đơn, trừ số lượng tồn kho sách theo từng item
        cart.getItems().forEach(item -> {
            String bookId = item.getBook().getId();
            int quantity = item.getQuantity();
            bookService.getBookById(bookId).ifPresent(book -> {
                int currentStock = book.getStock();
                int newStock = currentStock - quantity;
                if (newStock < 0) {
                    newStock = 0;
                }
                book.setStock(newStock);
                bookService.saveBook(book);
            });
        });

        return saved;
    }

    public List<Invoice> getInvoicesByUserId(String userId) {
        return invoiceRepository.findByUserId(userId);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public void deleteInvoice(String id) {
        invoiceRepository.deleteById(id);
    }

    public Optional<Invoice> getInvoiceById(String id) {
        return invoiceRepository.findById(id);
    }

    public Invoice saveInvoice(Invoice invoice) {
        invoice.setUpdatedAt(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }
}
