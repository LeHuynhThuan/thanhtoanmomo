package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Book;
import LeHuynhThuan.demo.entity.Category;
import LeHuynhThuan.demo.entity.Invoice;
import LeHuynhThuan.demo.entity.OrderStatus;
import LeHuynhThuan.demo.service.BookService;
import LeHuynhThuan.demo.service.CategoryService;
import LeHuynhThuan.demo.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final InvoiceService invoiceService;

    // ==================== DASHBOARD ====================
    @GetMapping
    public String dashboard(Model model) {
        // Calculate stats
        List<Invoice> allInvoices = invoiceService.getAllInvoices();
        
        double totalRevenue = allInvoices.stream()
                .filter(inv -> inv.getStatus() == OrderStatus.DELIVERED || 
                               inv.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
        
        long pendingOrders = allInvoices.stream()
                .filter(inv -> inv.getStatus() == OrderStatus.PENDING)
                .count();
        
        long processingOrders = allInvoices.stream()
                .filter(inv -> inv.getStatus() == OrderStatus.PROCESSING)
                .count();
        
        long shippedOrders = allInvoices.stream()
                .filter(inv -> inv.getStatus() == OrderStatus.SHIPPED)
                .count();
        
        long deliveredOrders = allInvoices.stream()
                .filter(inv -> inv.getStatus() == OrderStatus.DELIVERED)
                .count();
        
        // Top selling books
        List<Book> topBooks = bookService.getTopSellingBooks(5);
        
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("processingOrders", processingOrders);
        model.addAttribute("shippedOrders", shippedOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("topBooks", topBooks);
        model.addAttribute("totalOrders", allInvoices.size());
        
        return "admin/dashboard";
    }

    // ==================== BOOKS MANAGEMENT ====================
    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newBook", new Book());
        model.addAttribute("newCategory", new Category());
        return "admin/books-list";
    }

    @PostMapping("/books/save")
    public String saveBook(@Valid @ModelAttribute("newBook") Book book,
                           BindingResult result,
                           @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                           Model model) throws IOException {
        
        // Validate category exists
        if (book.getCategoryId() == null || book.getCategoryId().isEmpty()) {
            result.rejectValue("categoryId", "error.book", "Vui lòng chọn thể loại");
        } else if (!categoryService.existsById(book.getCategoryId())) {
            result.rejectValue("categoryId", "error.book", "Thể loại không tồn tại");
        }
        
        // Handle file upload
        if (imageFile == null || imageFile.isEmpty()) {
            result.rejectValue("image", "error.book", "Vui lòng chọn ảnh sách");
        } else {
            // Validate file size (max 5MB)
            if (imageFile.getSize() > 5 * 1024 * 1024) {
                result.rejectValue("image", "error.book", "Kích thước file không được vượt quá 5MB");
            }
            
            // Validate file type
            String contentType = imageFile.getContentType();
            if (contentType == null || (!contentType.equals("image/png") && 
                                        !contentType.equals("image/jpeg") && 
                                        !contentType.equals("image/jpg"))) {
                result.rejectValue("image", "error.book", "Chỉ cho phép file PNG, JPG, JPEG");
            }
            
            if (!result.hasErrors()) {
                // Convert to Base64
                String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
                String dataPrefix = (contentType != null && contentType.equals("image/png"))
                        ? "data:image/png;base64,"
                        : "data:image/jpeg;base64,";
                book.setImage(dataPrefix + base64Image);
                book.setImageFileName(imageFile.getOriginalFilename());
                book.setUploadedDate(LocalDateTime.now());
            }
        }
        
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("newCategory", new Category());
            model.addAttribute("showAddBookModal", true);
            return "admin/books-list";
        }
        
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable String id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    model.addAttribute("books", bookService.getAllBooks());
                    model.addAttribute("categories", categoryService.getAllCategories());
                    model.addAttribute("newBook", new Book());
                    model.addAttribute("newCategory", new Category());
                    return "admin/books-edit";
                })
                .orElse("redirect:/admin/books");
    }

    @PostMapping("/books/edit/{id}")
    public String updateBook(@PathVariable String id,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult result,
                             @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                             Model model) throws IOException {
        Book existingBook = bookService.getBookById(id).orElse(null);
        if (existingBook == null) {
            return "redirect:/admin/books";
        }
        
        // Validate category
        if (book.getCategoryId() == null || book.getCategoryId().isEmpty()) {
            result.rejectValue("categoryId", "error.book", "Vui lòng chọn thể loại");
        } else if (!categoryService.existsById(book.getCategoryId())) {
            result.rejectValue("categoryId", "error.book", "Thể loại không tồn tại");
        }
        
        // Handle file upload
        if (imageFile != null && !imageFile.isEmpty()) {
            if (imageFile.getSize() > 5 * 1024 * 1024) {
                result.rejectValue("image", "error.book", "Kích thước file không được vượt quá 5MB");
            }
            
            String contentType = imageFile.getContentType();
            if (contentType == null || (!contentType.equals("image/png") && 
                                        !contentType.equals("image/jpeg") && 
                                        !contentType.equals("image/jpg"))) {
                result.rejectValue("image", "error.book", "Chỉ cho phép file PNG, JPG, JPEG");
            }
            
            if (!result.hasErrors()) {
                String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
                String dataPrefix = (contentType != null && contentType.equals("image/png"))
                        ? "data:image/png;base64,"
                        : "data:image/jpeg;base64,";
                book.setImage(dataPrefix + base64Image);
                book.setImageFileName(imageFile.getOriginalFilename());
                book.setUploadedDate(LocalDateTime.now());
            } else {
                // Keep old image for redisplay when upload is invalid
                book.setImage(existingBook.getImage());
                book.setImageFileName(existingBook.getImageFileName());
                book.setUploadedDate(existingBook.getUploadedDate());
            }
        } else {
            // Keep old image if user didn't upload a new one
            book.setImage(existingBook.getImage());
            book.setImageFileName(existingBook.getImageFileName());
            book.setUploadedDate(existingBook.getUploadedDate());
        }
        
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("newBook", new Book());
            model.addAttribute("newCategory", new Category());
            return "admin/books-edit";
        }
        
        book.setId(id);
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    // ==================== CATEGORIES MANAGEMENT ====================
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@Valid @ModelAttribute("newCategory") Category category,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/categories";
        }

        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/add-modal")
    @ResponseBody
    public String addCategoryModal(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return "{\"success\": false, \"message\": \"Tên thể loại không được để trống\"}";
        }
        
        Category category = new Category();
        category.setName(name.trim());
        categoryService.saveCategory(category);
        
        return "{\"success\": true, \"id\": \"" + category.getId() + "\", \"name\": \"" + category.getName() + "\"}";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable String id, Model model) {
        // Remove category reference from books but keep books
        bookService.clearCategoryForBooks(id);

        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    // ==================== ORDERS MANAGEMENT ====================
    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        model.addAttribute("orders", invoices);
        model.addAttribute("OrderStatus", OrderStatus.class);
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable String id,
                                    @RequestParam OrderStatus newStatus,
                                    Model model) {
        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElse(null);
        
        if (invoice == null) {
            return "redirect:/admin/orders";
        }
        
        // Check if order is locked (COMPLETED or CANCELLED)
        if (invoice.getStatus() == OrderStatus.COMPLETED || 
            invoice.getStatus() == OrderStatus.CANCELLED) {
            model.addAttribute("error", "Không thể sửa đơn hàng đã hoàn thành hoặc đã hủy");
            model.addAttribute("orders", invoiceService.getAllInvoices());
            return "admin/orders";
        }
        
        invoice.setStatus(newStatus);
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceService.saveInvoice(invoice);
        
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetails(@PathVariable String id, Model model) {
        return invoiceService.getInvoiceById(id)
                .map(invoice -> {
                    model.addAttribute("order", invoice);
                    model.addAttribute("OrderStatus", OrderStatus.class);
                    return "admin/order-details";
                })
                .orElse("redirect:/admin/orders");
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable String id) {
        invoiceService.getInvoiceById(id).ifPresent(inv -> invoiceService.deleteInvoice(id));
        return "redirect:/admin/orders";
    }
}
