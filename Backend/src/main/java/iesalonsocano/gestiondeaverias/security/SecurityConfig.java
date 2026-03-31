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

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;

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
 * @version 1.1.0
 * @see JwtAuthenticationFilter
 * @see JwtTokenProvider
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilitar @PreAuthorize
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
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        // Permitir todas las peticiones de preflight (OPTIONS)
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        // Rutas públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // .requestMatchers("/api/incidencias/filtrar").permitAll() // Protegido: requiere autenticación
                        .requestMatchers("/error").permitAll() // Permitir ver errores del servidor
                        // El resto requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Manejador de errores de autenticación (401 Unauthorized).
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) -> {
            // Log para depuración
            // Acceso no autorizado registrado
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{ \"message\": \"Error: No autorizado. Debes iniciar sesión.\" }");
        };
    }

    /**
     * Bean que proporciona el codificador de contraseñas BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que proporciona el AuthenticationManager de Spring Security.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura CORS (Cross-Origin Resource Sharing) para permitir peticiones desde el frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Incluimos localhost y 127.0.0.1 con todos los puertos habituales de desarrollo
        configuration.setAllowedOrigins(List.of(
                "http://localhost",
                "http://localhost:5173",
                "http://localhost:3000",
                "http://127.0.0.1",
                "http://127.0.0.1:5173",
                "http://127.0.0.1:3000"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Permitir todos los headers
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
