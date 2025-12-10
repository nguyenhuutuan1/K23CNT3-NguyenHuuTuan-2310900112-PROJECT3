package K23cnt3.nht.project3.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class ConfigTest implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConfigTest.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== KIỂM TRA FILE application.properties ===");

        try {
            // Kiểm tra file có tồn tại không
            ClassPathResource resource = new ClassPathResource("application.properties");
            System.out.println("File exists: " + resource.exists());
            System.out.println("File path: " + resource.getPath());
            System.out.println("File URL: " + resource.getURL());

            // Đọc nội dung file
            if (resource.exists()) {
                InputStream inputStream = resource.getInputStream();
                Properties props = new Properties();
                props.load(inputStream);

                System.out.println("\n=== NỘI DUNG FILE ===");
                System.out.println("server.port: " + props.getProperty("server.port"));
                System.out.println("spring.datasource.url: " + props.getProperty("spring.datasource.url"));
                System.out.println("spring.datasource.username: " + props.getProperty("spring.datasource.username"));
                System.out.println("spring.datasource.password: " + props.getProperty("spring.datasource.password"));

                inputStream.close();
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
