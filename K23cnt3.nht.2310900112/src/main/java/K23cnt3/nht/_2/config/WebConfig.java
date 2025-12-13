package K23cnt3.nht._2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Ảnh tĩnh có sẵn trong static/images/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // 2. Ảnh upload mới vào uploads/images/
        registry.addResourceHandler("/uploaded-images/**")
                .addResourceLocations("file:uploads/images/");

        // 3. CSS
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // 4. JS
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
    }
}