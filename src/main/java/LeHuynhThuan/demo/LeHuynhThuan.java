package LeHuynhThuan.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class LeHuynhThuan {

    private static final Logger logger = LoggerFactory.getLogger(LeHuynhThuan.class);

    public static void main(String[] args) {
        SpringApplication.run(LeHuynhThuan.class, args);
    }

    @Bean
    public CommandLineRunner checkDatabase(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                String dbName = mongoTemplate.getDb().getName();
                logger.info("=================================================");
                logger.info("✅ KET NOI DATABASE MONGODB THANH CONG!");
                logger.info("   Database: {}", dbName);
                logger.info("=================================================");
            } catch (Exception e) {
                logger.error("❌ LOI KET NOI DATABASE: {}", e.getMessage());
            }
        };
    }

    public static void logStartup() {
        logger.info("");
        logger.info("╔═══════════════════════════════════════════════════════════╗");
        logger.info("║          WEBSITE BÁN SÁCH - LÊ HUỲNH THUẬN             ║");
        logger.info("╠═══════════════════════════════════════════════════════════╣");
        logger.info("║  🌐 TRANG CHỦ:        http://localhost:8080/            ║");
        logger.info("║  📚 DANH SÁCH SÁCH:  http://localhost:8080/books/list  ║");
        logger.info("║  📧 LIÊN HỆ:         http://localhost:8080/contact     ║");
        logger.info("║  🛒 GIỎ HÀNG:        http://localhost:8080/cart        ║");
        logger.info("║  🔐 ĐĂNG NHẬP:       http://localhost:8080/auth/login  ║");
        logger.info("║  📝 ĐĂNG KÝ:         http://localhost:8080/auth/register║");
        logger.info("╠═══════════════════════════════════════════════════════════╣");
        logger.info("║  👤 TÀI KHOẢN DEMO:                                    ║");
        logger.info("║     Admin: admin / admin123                             ║");
        logger.info("║     User:  user / user123                               ║");
        logger.info("╚═══════════════════════════════════════════════════════════╝");
        logger.info("");
    }
}
