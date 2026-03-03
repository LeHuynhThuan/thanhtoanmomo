package LeHuynhThuan.demo.repository;

import LeHuynhThuan.demo.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends MongoRepository<Category, String> {
    Category findByName(String name);
}
