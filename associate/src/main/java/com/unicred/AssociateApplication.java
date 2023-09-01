package com.unicred;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "API para Associados",
                version = "1.0.0", description = "API para operações sobre os associados."
        )
)
@SpringBootApplication
public class AssociateApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssociateApplication.class, args);
    }

}