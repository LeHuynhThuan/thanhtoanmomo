package LeHuynhThuan.demo.config;

import LeHuynhThuan.demo.LeHuynhThuan;
import LeHuynhThuan.demo.entity.Book;
import LeHuynhThuan.demo.entity.Category;
import LeHuynhThuan.demo.entity.User;
import LeHuynhThuan.demo.repository.IBookRepository;
import LeHuynhThuan.demo.repository.ICategoryRepository;
import LeHuynhThuan.demo.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(ICategoryRepository categoryRepository,
                                       IBookRepository bookRepository,
                                       IUserRepository userRepository,
                                       BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            // Khoi tao Categories neu chua co
            if (categoryRepository.count() == 0) {
                Category cat1 = new Category(null, "Cong nghe thong tin");
                Category cat2 = new Category(null, "Kinh te");
                Category cat3 = new Category(null, "Van hoc");
                Category cat4 = new Category(null, "Khoa hoc");
                Category cat5 = new Category(null, "Ngoai ngu");

                cat1 = categoryRepository.save(cat1);
                cat2 = categoryRepository.save(cat2);
                cat3 = categoryRepository.save(cat3);
                cat4 = categoryRepository.save(cat4);
                cat5 = categoryRepository.save(cat5);

                // Khoi tao Books mau
                if (bookRepository.count() == 0) {
                    Book book1 = new Book();
                    book1.setTitle("Lap trinh Java");
                    book1.setAuthor("Nguyen Van A");
                    book1.setPrice(150000);
                    book1.setImage("https://via.placeholder.com/200x280?text=Java");
                    book1.setDescription("Sach hoc lap trinh Java co ban den nang cao");
                    book1.setCategoryId(cat1.getId());
                    bookRepository.save(book1);

                    Book book2 = new Book();
                    book2.setTitle("Spring Boot in Action");
                    book2.setAuthor("Craig Walls");
                    book2.setPrice(250000);
                    book2.setImage("https://via.placeholder.com/200x280?text=Spring+Boot");
                    book2.setDescription("Huong dan xay dung ung dung voi Spring Boot");
                    book2.setCategoryId(cat1.getId());
                    bookRepository.save(book2);

                    Book book3 = new Book();
                    book3.setTitle("Python cho nguoi moi");
                    book3.setAuthor("Tran Van B");
                    book3.setPrice(120000);
                    book3.setImage("https://via.placeholder.com/200x280?text=Python");
                    book3.setDescription("Nhap mon lap trinh Python");
                    book3.setCategoryId(cat1.getId());
                    bookRepository.save(book3);

                    Book book4 = new Book();
                    book4.setTitle("Kinh te hoc vi mo");
                    book4.setAuthor("Le Van C");
                    book4.setPrice(180000);
                    book4.setImage("https://via.placeholder.com/200x280?text=Kinh+Te");
                    book4.setDescription("Giao trinh kinh te hoc vi mo");
                    book4.setCategoryId(cat2.getId());
                    bookRepository.save(book4);

                    Book book5 = new Book();
                    book5.setTitle("Marketing can ban");
                    book5.setAuthor("Pham Thi D");
                    book5.setPrice(135000);
                    book5.setImage("https://via.placeholder.com/200x280?text=Marketing");
                    book5.setDescription("Nguyen ly Marketing hien dai");
                    book5.setCategoryId(cat2.getId());
                    bookRepository.save(book5);

                    Book book6 = new Book();
                    book6.setTitle("Truyen Kieu");
                    book6.setAuthor("Nguyen Du");
                    book6.setPrice(85000);
                    book6.setImage("https://via.placeholder.com/200x280?text=Truyen+Kieu");
                    book6.setDescription("Tac pham van hoc tien Viet Nam");
                    book6.setCategoryId(cat3.getId());
                    bookRepository.save(book6);

                    Book book7 = new Book();
                    book7.setTitle("So Do");
                    book7.setAuthor("Vu Trong Phung");
                    book7.setPrice(95000);
                    book7.setImage("https://via.placeholder.com/200x280?text=So+Do");
                    book7.setDescription("Tieu thuyet trao phung noi tieng");
                    book7.setCategoryId(cat3.getId());
                    bookRepository.save(book7);

                    Book book8 = new Book();
                    book8.setTitle("Vat ly dai cuong");
                    book8.setAuthor("Hoang Van E");
                    book8.setPrice(160000);
                    book8.setImage("https://via.placeholder.com/200x280?text=Vat+Ly");
                    book8.setDescription("Giao trinh vat ly dai cuong");
                    book8.setCategoryId(cat4.getId());
                    bookRepository.save(book8);

                    Book book9 = new Book();
                    book9.setTitle("TOEIC 990");
                    book9.setAuthor("Kim Tae Hee");
                    book9.setPrice(200000);
                    book9.setImage("https://via.placeholder.com/200x280?text=TOEIC");
                    book9.setDescription("Luyen thi TOEIC dat diem cao");
                    book9.setCategoryId(cat5.getId());
                    bookRepository.save(book9);

                    Book book10 = new Book();
                    book10.setTitle("IELTS Academic");
                    book10.setAuthor("Simon");
                    book10.setPrice(220000);
                    book10.setImage("https://via.placeholder.com/200x280?text=IELTS");
                    book10.setDescription("Chinh phuc IELTS Academic");
                    book10.setCategoryId(cat5.getId());
                    bookRepository.save(book10);
                }
            }

            // Khoi tao Admin user neu chua co
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@bookstore.com");
                admin.setFullName("Administrator");
                admin.setPhone("0123456789");
                Set<String> adminRoles = new HashSet<>();
                adminRoles.add("ADMIN");
                adminRoles.add("USER");
                admin.setRoles(adminRoles);
                userRepository.save(admin);
                logger.info("✅ Tao tai khoan Admin: admin / admin123");
            }

            // Khoi tao User mau neu chua co
            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@bookstore.com");
                user.setFullName("Nguoi dung");
                user.setPhone("0987654321");
                Set<String> userRoles = new HashSet<>();
                userRoles.add("USER");
                user.setRoles(userRoles);
                userRepository.save(user);
                logger.info("✅ Tao tai khoan User: user / user123");
            }

            // Hien thi menu trang web
            LeHuynhThuan.logStartup();
        };
    }
}
