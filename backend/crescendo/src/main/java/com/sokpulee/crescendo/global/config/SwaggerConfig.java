package com.sokpulee.crescendo.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Crescendo API")
                .description("Crescendo application API documentation")
                .version("1.0");

        return new OpenAPI()
                .info(info);
    }
}
