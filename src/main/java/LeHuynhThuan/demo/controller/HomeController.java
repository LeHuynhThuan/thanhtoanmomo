package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.service.BookService;
import LeHuynhThuan.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Add student info
        model.addAttribute("studentName", "Lê Huỳnh Thuận");
        model.addAttribute("studentId", "MSSV");
        model.addAttribute("school", "HUTECH");
        model.addAttribute("faculty", "Công nghệ thông tin");
        
        // Fetch categories and books from database
        var categories = categoryService.getAllCategories();
        var allBooks = bookService.getAllBooks();
        
        // Get featured books (first 8 books or less)
        var featuredBooks = allBooks.size() > 8 ? allBooks.subList(0, 8) : allBooks;
        
        model.addAttribute("categories", categories);
        model.addAttribute("products", allBooks);
        model.addAttribute("featuredBooks", featuredBooks);
        
        return "home";
    }
}
