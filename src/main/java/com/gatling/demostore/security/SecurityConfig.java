package com.gatling.demostore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder());

    }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http
    //     .authorizeRequests()
    //     .antMatchers("/category/**").hasAnyRole("USER", "ADMIN")
    //     .antMatchers("/admin/**").hasAnyRole("ADMIN")
    //     .antMatchers("/").permitAll()
    //         .and()
    //             .formLogin()
    //                 .loginPage("/login")
    //         .and()
    //             .logout()
    //                 .logoutSuccessUrl("/")
    //         .and()
    //             .exceptionHandling()
    //                 .accessDeniedPage("/");

 //       .antMatchers("/**").hasAnyAuthority("USER");

        // Different way of writing it with Spring Security language 
        // http
        //     .authorizeRequests()
        //         .antMatchers("/category/**").access("hasRole('ROLE_USER')")
        //         .antMatchers("/").access("permitAll");
 //   }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http
            .requiresChannel()
            .anyRequest()
            .requiresSecure()
        .and()
            .authorizeRequests()
            .antMatchers("/**")
            .permitAll();
    }
    
}
