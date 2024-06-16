package com.yuanliubei.hotchpotch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@OpenAPIDefinition(
        info = @Info(
                title = "OpenApi specification - Paran",
                description = "OpenApi documentation for SpringBoot3",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        }
)
@Configuration
public class OpenApiConfig {
}
