package io.gatling.demostore.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerUiRedirectController {

    @RequestMapping("/swagger-ui")
    public String redirect() {
        return "redirect:/swagger-ui/index.html";
    }
}
