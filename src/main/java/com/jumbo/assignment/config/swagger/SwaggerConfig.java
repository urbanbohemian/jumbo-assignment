package com.jumbo.assignment.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(
        title = "${spring.application.name}",
        description = "${spring.application.description}"
))
@Configuration
public class SwaggerConfig {
}
