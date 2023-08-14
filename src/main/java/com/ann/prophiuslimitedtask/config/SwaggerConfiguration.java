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

/* This configuration class sets up Swagger documentation for the API.
It uses annotations from the io.swagger.v3.oas.annotations package to provide metadata about the API,
such as its title, version, contact information, and license details.
Swagger generates API documentation based on this configuration,
 making it easier to understand and interact with your API.
 */