package de.telran.mycryptowallet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration for Web Security
 *
 * @author Alexander Isai on 18.01.2024.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(HttpMethod.GET, "/show-all-users").hasRole("ADMIN");
                    request.anyRequest().permitAll();
                })
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasis -> httpBasis.init(http));
        return http.build();
    }
}
//TODO повторить и разобраться в деталях авторизации
