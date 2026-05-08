package com.nhiennhatt.bookstoreapi.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public ObjectMapperProvider springdocObjectMapperProvider(SpringDocConfigProperties properties) {
        ObjectMapperProvider provider = new ObjectMapperProvider(properties);

        JsonNullableModule module = new JsonNullableModule();
        provider.jsonMapper().registerModule(module);
        provider.yamlMapper().registerModule(module);

        return provider;
    }

    @Bean
    public JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components().addSecuritySchemes(
                "bearer-auth",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
        ));
    }

}
