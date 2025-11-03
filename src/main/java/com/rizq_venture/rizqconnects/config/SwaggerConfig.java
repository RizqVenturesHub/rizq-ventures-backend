package com.rizq_venture.rizqconnects.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI rizqConnectAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rizq Connect API")
                        .description("Rizq Venture - LinkedIn-like platform backend API documentation")
                        .version("v1.0"));
    }
}
