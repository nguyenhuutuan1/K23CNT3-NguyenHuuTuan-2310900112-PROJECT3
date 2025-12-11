package K23cnt3.nht._2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL path /uploads/** to physical directory
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/")
                .setCachePeriod(3600)
                .resourceChain(true);

        // Thêm cache control cho static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }

    /**
     * Get upload directory path
     */
    public Path getUploadPath() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Get product images upload path
     */
    public Path getProductUploadPath() {
        return getUploadPath().resolve("products");
    }

    /**
     * Get avatar upload path
     */
    public Path getAvatarUploadPath() {
        return getUploadPath().resolve("avatars");
    }
}