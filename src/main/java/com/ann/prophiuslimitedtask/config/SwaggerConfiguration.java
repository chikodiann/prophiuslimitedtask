package com.ann.prophiuslimitedtask.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(

        info = @Info(
                contact = @Contact(name = "Chikodinaka Ann Anyanwu", email = "chikodiann@gmail.com", url = "https://github.com/chikodiann/prophiuslimitedtask"),
                description = "API documentation for Prophius Limited Task.",
                title = "Prophius Limited Task API",
                version = "1.0",
                license = @License(name = "Apache License", url = "https://www.apache.org/licenses/LICENSE-2"),
                termsOfService = "Terms of Service"
        )

)
public class SwaggerConfiguration {
}
