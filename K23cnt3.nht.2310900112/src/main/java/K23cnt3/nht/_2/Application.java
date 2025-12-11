package K23cnt3.nht._2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("K23cnt3.nht._2.entity")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("=========================================");
        System.out.println("🛒 CỬA HÀNG TẠP HÓA ONLINE");
        System.out.println("👉 Website: http://localhost:8080");
        System.out.println("👉 Admin:    http://localhost:8080/admin");
        System.out.println("👉 Database: taphoa_nht");
        System.out.println("=========================================");
    }
}