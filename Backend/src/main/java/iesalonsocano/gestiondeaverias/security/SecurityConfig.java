package iesalonsocano.gestiondeaverias.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración principal de Spring Security.
 * <p>
 * Esta clase configura:
 * <ul>
 *   <li>Autenticación basada en JWT (sin sesiones)</li>
 *   <li>Autorización de endpoints por roles</li>
 *   <li>CORS para permitir peticiones desde el frontend</li>
 *   <li>Cifrado de contraseñas con BCrypt</li>
 * </ul>
 * </p>
 *
 * <p>
 * Configuración de seguridad:
 * <ul>
 *   <li>Endpoints públicos: /api/auth/**, /swagger-ui/**, /v3/api-docs/**</li>
 *   <li>Resto de endpoints: requieren autenticación</li>
 * </ul>
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see JwtAuthenticationFilter
 * @see JwtTokenProvider
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param jwtAuthenticationFilter filtro JWT para interceptar peticiones
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configura la cadena de filtros de seguridad.
     * <p>
     * Establece:
     * <ul>
     *   <li>Política de sesión STATELESS (sin sesiones HTTP)</li>
     *   <li>Deshabilitación de CSRF (no necesario con JWT)</li>
     *   <li>Reglas de autorización por endpoints</li>
     *   <li>Filtro JWT antes del filtro de autenticación estándar</li>
     * </ul>
     * </p>
     *
     * @param http builder de configuración de HTTP security
     * @return SecurityFilterChain configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // habilitamos CORS
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        /*
                        // Rutas protegidas de ejemplo
                        .requestMatchers(HttpMethod.POST, "/api/aulas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/aulas/**").hasRole("ADMIN")
                        */
                        // El resto requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean que proporciona el codificador de contraseñas BCrypt.
     * <p>
     * BCrypt es un algoritmo de hashing adaptativo diseñado para cifrar contraseñas.
     * Incluye un salt automático y es resistente a ataques de fuerza bruta.
     * </p>
     *
     * @return instancia de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que proporciona el AuthenticationManager de Spring Security.
     * <p>
     * Gestiona el proceso de autenticación, validando credenciales de usuario.
     * </p>
     *
     * @param config configuración de autenticación
     * @return AuthenticationManager configurado
     * @throws Exception si ocurre un error al obtener el manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura CORS (Cross-Origin Resource Sharing) para permitir peticiones desde el frontend.
     * <p>
     * Permite:
     * <ul>
     *   <li>Origen: http://localhost:5173 (frontend React/Vite)</li>
     *   <li>Métodos: GET, POST, PUT, DELETE, OPTIONS</li>
     *   <li>Todas las cabeceras</li>
     *   <li>Credenciales (cookies, headers de autenticación)</li>
     * </ul>
     * </p>
     *
     * @return CorsConfigurationSource configurada
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // si usas cookies o auth

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
