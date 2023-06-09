package jp.co.sunarch.demo.todomvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
        .httpBasic(withDefaults())
        .formLogin(withDefaults())
        .logout(withDefaults())
        .csrf(csrf -> csrf.disable());
    return http.build();
  }
}
