package com.dsw02.empleado.infrastructure.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        SecurityScheme basicAuthScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("basic");

        return new OpenAPI()
            .info(new Info().title("API CRUD Empleados").version("1.0.0"))
            .components(new Components().addSecuritySchemes("basicAuth", basicAuthScheme))
            .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }
}
