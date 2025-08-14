package com.eteration.simplebanking.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Simple Banking API")
                        .description("Doğanay Tay - software develıoer (iş poziyonu) simplebanking case çalışması")
                        .version("v1")
                        .contact(new Contact()
                                .name("Doğanay Tay")
                                .email("doganaytay@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Assignment Readme")
                        .url("https://github.com/doganaytay1097"));
    }
}
