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

    public Invoice createInvoice(Invoice invoice, Cart cart) {
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
        invoice.setTotalAmount(cart.getTotal());
        invoice.setOrderDate(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setStatus(OrderStatus.PENDING);
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoicesByUserId(String userId) {
        return invoiceRepository.findByUserId(userId);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(String id) {
        return invoiceRepository.findById(id);
    }

    public Invoice saveInvoice(Invoice invoice) {
        invoice.setUpdatedAt(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }
}
