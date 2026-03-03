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
                    bookRepository.save(new Book(null, "Lap trinh Java", "Nguyen Van A", 150000, "https://via.placeholder.com/200x280?text=Java", "Sach hoc lap trinh Java co ban den nang cao", cat1.getId(), null));
                    bookRepository.save(new Book(null, "Spring Boot in Action", "Craig Walls", 250000, "https://via.placeholder.com/200x280?text=Spring+Boot", "Huong dan xay dung ung dung voi Spring Boot", cat1.getId(), null));
                    bookRepository.save(new Book(null, "Python cho nguoi moi", "Tran Van B", 120000, "https://via.placeholder.com/200x280?text=Python", "Nhap mon lap trinh Python", cat1.getId(), null));
                    bookRepository.save(new Book(null, "Kinh te hoc vi mo", "Le Van C", 180000, "https://via.placeholder.com/200x280?text=Kinh+Te", "Giao trinh kinh te hoc vi mo", cat2.getId(), null));
                    bookRepository.save(new Book(null, "Marketing can ban", "Pham Thi D", 135000, "https://via.placeholder.com/200x280?text=Marketing", "Nguyen ly Marketing hien dai", cat2.getId(), null));
                    bookRepository.save(new Book(null, "Truyen Kieu", "Nguyen Du", 85000, "https://via.placeholder.com/200x280?text=Truyen+Kieu", "Tac pham van hoc tien Viet Nam", cat3.getId(), null));
                    bookRepository.save(new Book(null, "So Do", "Vu Trong Phung", 95000, "https://via.placeholder.com/200x280?text=So+Do", "Tieu thuyet trao phung noi tieng", cat3.getId(), null));
                    bookRepository.save(new Book(null, "Vat ly dai cuong", "Hoang Van E", 160000, "https://via.placeholder.com/200x280?text=Vat+Ly", "Giao trinh vat ly dai cuong", cat4.getId(), null));
                    bookRepository.save(new Book(null, "TOEIC 990", "Kim Tae Hee", 200000, "https://via.placeholder.com/200x280?text=TOEIC", "Luyen thi TOEIC dat diem cao", cat5.getId(), null));
                    bookRepository.save(new Book(null, "IELTS Academic", "Simon", 220000, "https://via.placeholder.com/200x280?text=IELTS", "Chinh phuc IELTS Academic", cat5.getId(), null));
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
