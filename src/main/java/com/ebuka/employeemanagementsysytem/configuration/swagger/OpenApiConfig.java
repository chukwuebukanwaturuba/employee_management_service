package com.ebuka.employeemanagementsysytem.configuration.swagger;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Employee Management System API")
                        .description("API documentation for Employee Management System")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }


    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/v1/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi profileApi() {
        return GroupedOpenApi.builder()
                .group("Profile")
                .pathsToMatch("/api/v1/profile/**")
                .build();
    }
}
