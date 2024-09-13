package uz.pdp.springboot.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "My first Application",
                description = "Bu dasturda Tasklar manege qilinadi",
                termsOfService = "/termsOfApp",
                contact = @Contact(
                        name = "G'ayrat",
                        url = "https://codeschool.uz",
                        email = "codeschool@gmail.com"
                ),
                version = "v0.0.1"
        ),
        servers = {
                @Server(url = "http://localhost:8080",description = "For Local Mode"),
                @Server(url = "http://123.10.20.4.18:8080",description = "For Production Mode")
        }
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        description = "You have to add JWT Token",
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
