package com.migueljuncalp.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI inventoryOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Inventory Service API")
                .version("v1")
                .description("API de práctica para registrar movimientos y consultar stock procesado mediante Kafka.")
                .contact(new Contact().name("Backend Interview Practice")));
    }
}
