// Tạo file src/main/java/K23cnt3/nht/_2/init/DefaultImagesInitializer.java
package K23cnt3.nht._2.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class DefaultImagesInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        createDefaultImages();
    }

    private void createDefaultImages() throws Exception {
        // Tạo thư mục
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadDir.resolve("products"));
        Files.createDirectories(uploadDir.resolve("avatars/default"));

        // Tạo ảnh sản phẩm mặc định
        Path defaultProduct = uploadDir.resolve("products/default-product.jpg");
        if (!Files.exists(defaultProduct)) {
            try (InputStream is = new ClassPathResource("static/images/products/default-product.jpg").getInputStream()) {
                Files.copy(is, defaultProduct, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Tạo avatar mặc định
        Path defaultAvatar = uploadDir.resolve("avatars/default/default-avatar.png");
        if (!Files.exists(defaultAvatar)) {
            try (InputStream is = new ClassPathResource("static/images/avatars/default-avatar.png").getInputStream()) {
                Files.copy(is, defaultAvatar, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Tạo ảnh placeholder
        createPlaceholderImages();
    }

    private void createPlaceholderImages() throws Exception {
        // Tạo ảnh placeholder cho sản phẩm
        for (int i = 1; i <= 10; i++) {
            Path productImage = Paths.get("uploads/products/placeholder-" + i + ".jpg");
            if (!Files.exists(productImage)) {
                // Tạo ảnh đơn giản bằng code hoặc copy từ resource
                String placeholderContent = "Product Placeholder " + i;
                Files.writeString(productImage, placeholderContent);
            }
        }
    }
}