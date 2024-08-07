package com.sokpulee.crescendo.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "http://localhost:8000",
                        "https://localhost:3000",
                        "https://localhost:8080",
                        "https://localhost:8000",
                        "http://i11b108.p.ssafy.io:3000",
                        "http://i11b108.p.ssafy.io:8000",
                        "http://i11b108.p.ssafy.io:8080",
                        "https://i11b108.p.ssafy.io",
                        "https://i11b108.p.ssafy.io:3000",
                        "https://i11b108.p.ssafy.io:8000",
                        "https://i11b108.p.ssafy.io:8080"
                )
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.LOCATION, "Authorization", "Set-Cookie")
                .allowCredentials(true);
    }
}
