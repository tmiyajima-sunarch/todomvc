package jp.co.sunarch.demo.todomvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class TodomvcApplication {

  public static void main(String[] args) {
    SpringApplication.run(TodomvcApplication.class, args);
  }

  @Configuration
  public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
          .mvcMatchers("/api/**").authenticated()
          .anyRequest().permitAll()
          .and()
          .httpBasic()
          .and()
          .formLogin()
          .and()
          .logout()
          .and()
          .csrf().disable();
    }
  }
}
