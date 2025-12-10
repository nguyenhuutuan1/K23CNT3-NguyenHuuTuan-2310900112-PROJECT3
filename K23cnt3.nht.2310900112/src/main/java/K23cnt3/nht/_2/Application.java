package K23cnt3.nht._2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("✅ Ứng dụng cửa hàng tạp hóa đã khởi động!");
        System.out.println("👉 Truy cập: http://localhost:8080/taphoa");
    }
}