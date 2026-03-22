package iesalonsocano.gestiondeaverias.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación automática de la API REST.
 * <p>
 * Esta configuración genera una interfaz web interactiva donde se pueden visualizar
 * y probar todos los endpoints de la API.
 * </p>
 *
 * <p>
 * La documentación está disponible en:
 * <ul>
 *   <li>UI interactiva: <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a></li>
 *   <li>JSON de OpenAPI: <a href="http://localhost:8080/v3/api-docs">http://localhost:8080/v3/api-docs</a></li>
 * </ul>
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see OpenAPI
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura la información general de la API para la documentación OpenAPI.
     * <p>
     * Define metadatos como título, descripción, versión e información de contacto
     * que se mostrarán en la interfaz de Swagger UI.
     * </p>
     *
     * @return instancia configurada de OpenAPI con la información de la API
     */
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
