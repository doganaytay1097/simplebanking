package com.eteration.simplebanking.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Simple Banking API")
                        .description("Credit/Debit & Account endpoints for simple banking assignment")
                        .version("v1")
                        .contact(new Contact()
                                .name("Simple Banking Team")
                                .email("support@example.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Assignment Readme")
                        .url("https://example.com/docs"));
    }
}
