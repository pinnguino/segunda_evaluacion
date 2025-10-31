package com.pinnguino.academy.segunda_evaluacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Sistema de Votación")
                        .version("1.0")
                        .description("API REST para gestionar un sistema de votación con candidatos, partidos políticos y votos")
                        .contact(new Contact()
                                .name("pinnguino")
                                .email("s.gallardogaston@gmail.com")));
    }
}