package LeHuynhThuan.demo.service;

import LeHuynhThuan.demo.entity.Book;
import LeHuynhThuan.demo.entity.Category;
import LeHuynhThuan.demo.repository.IBookRepository;
import LeHuynhThuan.demo.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final IBookRepository bookRepository;
    private final ICategoryRepository categoryRepository;

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        books.forEach(this::populateCategory);
        return books;
    }

    public Optional<Book> getBookById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        book.ifPresent(this::populateCategory);
        return book;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = bookRepository.searchBooks(keyword);
        books.forEach(this::populateCategory);
        return books;
    }

    public List<Book> getBooksByCategory(String categoryId) {
        List<Book> books = bookRepository.findByCategoryId(categoryId);
        books.forEach(this::populateCategory);
        return books;
    }

    public void clearCategoryForBooks(String categoryId) {
        List<Book> books = bookRepository.findByCategoryId(categoryId);
        for (Book book : books) {
            book.setCategoryId(null);
            // category will be repopulated as null
            bookRepository.save(book);
        }
    }

    public long countByCategory(String categoryId) {
        return bookRepository.findByCategoryId(categoryId).size();
    }

    public List<Book> getTopSellingBooks(int limit) {
        // For now, return the first N books
        // In a production app, you'd query from InvoiceItems to find highest sold books
        List<Book> books = bookRepository.findAll();
        books.forEach(this::populateCategory);
        return books.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private void populateCategory(Book book) {
        if (book.getCategoryId() != null) {
            categoryRepository.findById(book.getCategoryId())
                    .ifPresent(book::setCategory);
        }
    }
}
