package LeHuynhThuan.demo.repository;

import LeHuynhThuan.demo.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookRepository extends MongoRepository<Book, String> {

    List<Book> findByCategoryId(String categoryId);

    @Query("{ $or: [ " +
            "{ 'title': { $regex: ?0, $options: 'i' } }, " +
            "{ 'author': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Book> searchBooks(String keyword);

    @Query("{ $or: [ " +
            "{ 'title': { $regex: ?0, $options: 'i' } }, " +
            "{ 'author': { $regex: ?0, $options: 'i' } }, " +
            "{ 'categoryId': ?1 } " +
            "] }")
    List<Book> searchBooksWithCategory(String keyword, String categoryId);
}
