package LeHuynhThuan.demo.entity;

import LeHuynhThuan.demo.validator.ValidCategoryId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 1, max = 200, message = "Tiêu đề phải từ 1 đến 200 ký tự")
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    private String author;

    @Positive(message = "Giá phải là số dương")
    private double price;

    private String image; // Base64 encoded image data

    private String imageFileName; // Original filename

    private LocalDateTime uploadedDate; // When image was uploaded

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    @ValidCategoryId
    private String categoryId;

    @Transient
    private Category category;

    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private int stock;
}

