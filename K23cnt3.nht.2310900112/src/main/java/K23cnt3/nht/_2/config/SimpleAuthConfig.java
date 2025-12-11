package K23cnt3.nht._2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SimpleAuthConfig implements WebMvcConfigurer {

    // Không cần Spring Security, sử dụng session đơn giản
    // Kiểm tra đăng nhập trong Controller bằng @SessionAttribute

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Có thể thêm interceptor để kiểm tra đăng nhập
        // registry.addInterceptor(authInterceptor())
        //         .addPathPatterns("/tai-khoan/**", "/admin/**")
        //         .excludePathPatterns("/tai-khoan/dang-nhap", "/tai-khoan/dang-ky");
    }

    // @Bean
    // public AuthInterceptor authInterceptor() {
    //     return new AuthInterceptor();
    // }
}