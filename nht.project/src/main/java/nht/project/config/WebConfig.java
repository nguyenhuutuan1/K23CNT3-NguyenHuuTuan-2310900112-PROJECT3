package nht.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Cấu hình CORS (Cross-Origin Resource Sharing)
     * Cho phép truy cập API từ các domain khác
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    /**
     * Cấu hình Static Resources
     * Mapping các thư mục chứa file tĩnh (CSS, JS, images)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // CSS
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // JavaScript
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // Images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // Webjars (Bootstrap, jQuery, etc.)
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Cấu hình View Controllers
     * Map trực tiếp URL với template mà không cần Controller
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Error pages
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/403").setViewName("error/403");
        registry.addViewController("/404").setViewName("error/404");
        registry.addViewController("/500").setViewName("error/500");
    }
}