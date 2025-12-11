package K23cnt3.nht._2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "K23cnt3.nht._2.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // Spring Boot tự động cấu hình DataSource từ application.properties
    // Không cần cấu hình thủ công
}