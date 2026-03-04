package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Book;
import LeHuynhThuan.demo.service.BookService;
import LeHuynhThuan.demo.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "books/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "books/add";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String categoryId,
                               Model model) {
        List<Book> books;
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookService.searchBooks(keyword.trim());
        } else if (categoryId != null && !categoryId.isEmpty()) {
            books = bookService.getBooksByCategory(categoryId);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", categoryId);
        return "books/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    model.addAttribute("categories", categoryService.getAllCategories());
                    return "books/edit";
                })
                .orElse("redirect:/books/list");
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable String id,
                            @Valid @ModelAttribute Book book,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "books/edit";
        }
        book.setId(id);
        bookService.saveBook(book);
        return "redirect:/books/list";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addBook(@Valid @ModelAttribute Book book,
                          BindingResult result,
                          Model model) {
        if (book.getCategoryId() == null || book.getCategoryId().isEmpty()) {
            result.rejectValue("categoryId", "error.book", "Vui lòng chọn thể loại");
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "books/add";
        }

        bookService.saveBook(book);
        return "redirect:/books/list";
    }

    @GetMapping("/{id}")
    public String viewBookDetail(@PathVariable String id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "books/detail";
                })
                .orElse("redirect:/books/list");
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "redirect:/books/list";
    }
}
