package K23cnt3.nht._2; // Thay đổi package này theo dự án của bạn

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.taphoa.entity") // Quét các entity
@EnableJpaRepositories(basePackages = "com.taphoa.repository") // Quét các repository
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("=========================================");
        System.out.println("🚀 ỨNG DỤNG BÁN TẠP HÓA ĐÃ KHỞI ĐỘNG");
        System.out.println("=========================================");
        System.out.println("📊 Truy cập các đường dẫn sau:");
        System.out.println("👉  Frontend: http://localhost:8080");
        System.out.println("👉  Admin:    http://localhost:8080/admin");
        System.out.println("👉  H2 Console (nếu enabled): http://localhost:8080/h2-console");
        System.out.println("👉  API Docs: http://localhost:8080/swagger-ui.html");
        System.out.println("=========================================");
    }
}