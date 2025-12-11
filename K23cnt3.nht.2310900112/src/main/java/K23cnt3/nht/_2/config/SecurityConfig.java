package K23cnt3.nht._2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tạm thời disable CSRF cho dễ dev
                .authorizeHttpRequests(auth -> auth
                        // Public URLs - ai cũng truy cập được
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/san-pham/**",
                                "/tim-kiem", "/gioi-thieu", "/lien-he",
                                "/tai-khoan/dang-nhap", "/tai-khoan/dang-ky",
                                "/gio-hang/**", "/thanh-toan").permitAll()

                        // Admin URLs - chỉ admin truy cập được
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Authenticated URLs - cần đăng nhập
                        .requestMatchers("/tai-khoan/don-hang/**", "/tai-khoan/profile").authenticated()

                        // Mọi request khác đều cho phép (tạm thời)
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/tai-khoan/dang-nhap")
                        .loginProcessingUrl("/tai-khoan/dang-nhap")
                        .defaultSuccessUrl("/")
                        .failureUrl("/tai-khoan/dang-nhap?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/tai-khoan/dang-xuat")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}