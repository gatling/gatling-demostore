package io.gatling.demostore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Service;

@Order(2)
@Service
@EnableWebSecurity
public class WebsiteSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final CookieSecurityContextRepository cookieSecurityContextRepository;

    public WebsiteSecurityConfig(UserDetailsService userDetailsService, CookieSecurityContextRepository cookieSecurityContextRepository) {
        this.userDetailsService = userDetailsService;
        this.cookieSecurityContextRepository = cookieSecurityContextRepository;
    }


    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Bean
    protected CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
        cookieCsrfTokenRepository.setCookiePath("/");
        return cookieCsrfTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .csrfTokenRepository(cookieCsrfTokenRepository());

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Avoid creating session in memory
                .and().securityContext().securityContextRepository(cookieSecurityContextRepository)
                .and().logout().permitAll().deleteCookies(SignedUserInfoCookie.NAME);

        http
                .requiresChannel()
                .anyRequest();
        http
                .authorizeRequests()
                .antMatchers("/cart/view").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/").permitAll();
        http
                .formLogin()
                .loginPage("/login");
        http
                .logout()
                .logoutSuccessUrl("/");
        http
                .exceptionHandling()
                .accessDeniedPage("/");
    }
}
