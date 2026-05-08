package com.company.crmticketing.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Full Sample API")
                        .description("""
                                ## API Documentation for Spring Boot Full Sample Application
                                
                                This is a comprehensive REST API with JWT authentication, 
                                role-based access control, and permission-based authorization.
                                
                                ### Authentication
                                Use the `/api/v1/auth/login` endpoint to obtain a JWT token.
                                Include the token in subsequent requests using the `Authorization: Bearer <token>` header.
                                
                                ### Authorization
                                Access to endpoints is controlled by:
                                - **Role-based**: `@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")`
                                - **Permission-based**: `@PreAuthorize("hasPermission('READ_USER')")`
                                - **Owner-based**: `@PreAuthorize("@securityUserService.isCurrentUser(#id, authentication)")`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Spring Boot Full Sample Team")
                                .email("support@example.com")
                                .url("https://github.com/example/springboot-fullsample"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Production Server")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token. Example: `eyJhbGciOiJIUzUxMiJ9...`")));
    }
}