package io.gatling.demostore.api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI api() {
        Contact contact = new Contact();
        contact.setName("Gatling");
        contact.setUrl("https://gatling.io/");

        return new OpenAPI()
          .info(new Info()
            .title("Gatling DemoStore API")
            .description("This is an API for a fictional / dummy eCommerce store that sells eyeglass cases. The create/update API endpoints only simulate writes, their results do not actually get persisted.")
            .contact(contact)
            .version("1.0.0")
          )
          .components(new Components().addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
            .name(SECURITY_SCHEME)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
          ));
    }
}
