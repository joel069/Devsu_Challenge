package Customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info()
                .title("DEVSU API REST DOCUMENTATION")
                .version("1.0")
                .contact(new Contact()
                        .name("DEVSU")
                        .url("https://devsu.com"))
                .description("Documentation for Devsu API REST")
                .termsOfService("https://www.sample.com/terms")
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org"));
        openAPI.setInfo(info);
        return openAPI;
    }
}
