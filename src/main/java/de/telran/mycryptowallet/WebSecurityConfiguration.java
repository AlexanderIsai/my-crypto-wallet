package de.telran.mycryptowallet;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration for Web Security
 *
 * @author Alexander Isai on 18.01.2024.
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/index.html").permitAll();
                    request.requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN");
                    request.requestMatchers(HttpMethod.POST, "/currency/add-new-currency").hasRole("ADMIN");

                    request.anyRequest().permitAll();
                })
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasis -> httpBasis.init(http));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        UserDetailsService userDetailsService = (username) -> {
            User user = userRepository.findUserByUserName(username);
            if (user != null) {
                return user;
            }
            else {
                throw new UsernameNotFoundException("ERROR");
            }
        };

        return userDetailsService;

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
}
//TODO повторить и разобраться в деталях авторизации
