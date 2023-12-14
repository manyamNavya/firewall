package com.firewall.app.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:7075/");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("navya@gmail.com");
        contact.setName("Navya Manyamgiri");
        contact.setUrl("http://locathost:7075/");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Firewall Management APIs")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage Firewall Rules").termsOfService("http:localhost:7075/")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(Arrays.asList(devServer));
    }
}