package io.gatling.demostore.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket scrumAllyApi() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .paths(regex("/api.*"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(Collections.singletonList(authenticationScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Gatling DemoStore API")
                .description("This is an API for a fictional / dummy eCommerce store that sells eyeglass cases. The create/update API endpoints only simulate writes, their results do not actually get persisted.")
                .contact(new Contact("Gatling", "https://gatling.io/", null))
                .build();
    }

    private HttpAuthenticationScheme authenticationScheme() {
        return HttpAuthenticationScheme
                .JWT_BEARER_BUILDER
                .name("JWT")
                .build();
    }

    private SecurityContext securityContext() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope("global", "accessEverything")};
        List<SecurityReference> securityReferences =
                Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
        return SecurityContext.builder().securityReferences(securityReferences).build();
    }
}
