package LeHuynhThuan.demo.validator;

import LeHuynhThuan.demo.repository.ICategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryIdValidator implements ConstraintValidator<ValidCategoryId, String> {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public boolean isValid(String categoryId, ConstraintValidatorContext context) {
        if (categoryId == null || categoryId.isEmpty()) {
            return true;
        }
        return categoryRepository.existsById(categoryId);
    }
}
