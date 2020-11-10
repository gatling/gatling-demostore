package com.gatling.demostore;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    // @Override
    // public void addViewControllers(ViewControllerRegistry registry) {
    //     registry.addViewController("/").setViewName("home");
    // }

    //TODO - THIS MIGHT BE WRONG - https://www.baeldung.com/spring-mvc-static-resources
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
        .addResourceHandler("/resources/**")
        .addResourceLocations("classpath:/media/");
    }
    
}
