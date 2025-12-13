package K23cnt3.nht._2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        System.out.println("=========================================");
        System.out.println("HỆ THỐNG QUẢN LÝ TẠP HÓA NHT");
        System.out.println("Version: 1.0.0");
        System.out.println("Developer: Nguyen Huu Tuan");
        System.out.println("Student ID: 2310900112");
        System.out.println("URL: http://localhost:8080/taphoa");
        System.out.println("=========================================");
    }
}