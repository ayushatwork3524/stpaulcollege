package com.main.stpaul.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ST Paul API")
                        .version("1.0")
                        .description("API documentation for ST Paul project")
                        .termsOfService("https://example.com/terms")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Support Team")
                                .email("support@example.com")
                                .url("https://example.com"))
                );
    }
}
