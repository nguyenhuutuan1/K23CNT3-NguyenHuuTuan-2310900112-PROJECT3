package K23cnt3.nht.project3.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/taphoa_nht?useSSL=false&serverTimezone=UTC")
                .username("root")
                .password("123456")  // ĐỔI THÀNH PASSWORD CỦA BẠN
                .build();
    }
}