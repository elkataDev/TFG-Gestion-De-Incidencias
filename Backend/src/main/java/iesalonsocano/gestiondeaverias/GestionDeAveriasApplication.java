package iesalonsocano.gestiondeaverias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot para la Gestión de Averías del IES Alonso Cano.
 * <p>
 * Esta aplicación REST API permite gestionar:
 * <ul>
 *   <li>Aulas y espacios del centro educativo</li>
 *   <li>Incidencias técnicas reportadas por usuarios</li>
 *   <li>Inventario de equipamiento informático</li>
 *   <li>Usuarios del sistema con diferentes roles</li>
 * </ul>
 * </p>
 *
 * <p>
 * La aplicación utiliza:
 * <ul>
 *   <li>Spring Security con JWT para autenticación y autorización</li>
 *   <li>Spring Data JPA para persistencia de datos</li>
 *   <li>Swagger/OpenAPI para documentación de la API</li>
 * </ul>
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
public class GestionDeAveriasApplication {

    /**
     * Punto de entrada principal de la aplicación.
     * <p>
     * Inicializa el contexto de Spring Boot y arranca el servidor embebido.
     * </p>
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(GestionDeAveriasApplication.class, args);
    }
}