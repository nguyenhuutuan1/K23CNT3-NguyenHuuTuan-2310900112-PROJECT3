package K23cnt3.nht._2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình đường dẫn cho static resources
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/uploads/", "file:uploads/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Cấu hình các view controller đơn giản
        registry.addViewController("/dang-nhap").setViewName("redirect:/tai-khoan/dang-nhap");
        registry.addViewController("/dang-ky").setViewName("redirect:/tai-khoan/dang-ky");
        registry.addViewController("/admin").setViewName("redirect:/admin/");

        // Error pages
        registry.addViewController("/403").setViewName("error/403");
        registry.addViewController("/404").setViewName("error/404");
        registry.addViewController("/500").setViewName("error/500");
    }
}