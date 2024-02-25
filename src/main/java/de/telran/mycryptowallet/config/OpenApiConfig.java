package de.telran.mycryptowallet.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;


/**
 * description
 *
 * @author Alexander Isai on 19.02.2024.
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                ))
                .info(new Info().title("My Crypto Wallet API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }
}
