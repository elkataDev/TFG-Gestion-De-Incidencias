package iesalonsocano.gestiondeaverias.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /*Este archivo genera automáticamente la documentación de tu API en Swagger UI, accesible en tu navegador en
    * http://localhost:8080/swagger-ui.html
    *
    * This file automatically generates your API documentation in Swagger UI, accessible in your browser at
    * http://localhost:8080/swagger-ui.html*/

    @Bean
    public OpenAPI gestionDeAveriasAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Averías - IES Alonso Cano")
                        .description("API REST para la gestión de aulas, incidencias, inventario y usuarios.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tu nombre")
                                .email("tunombre@correo.com")
                                .url("https://github.com/tuusuario")));
    }
}
