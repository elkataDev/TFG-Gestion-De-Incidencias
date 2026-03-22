package iesalonsocano.gestiondeaverias.security;

import iesalonsocano.gestiondeaverias.Services.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que intercepta cada petición HTTP.
 * <p>
 * Este filtro:
 * <ol>
 *   <li>Extrae el token JWT de la cabecera Authorization</li>
 *   <li>Valida el token</li>
 *   <li>Extrae el usuario del token</li>
 *   <li>Establece el contexto de seguridad de Spring</li>
 * </ol>
 * </p>
 *
 * <p>
 * El token debe enviarse en la cabecera: {@code Authorization: Bearer <token>}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see JwtTokenProvider
 * @see OncePerRequestFilter
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Filtra cada petición HTTP para validar y procesar el token JWT.
     * <p>
     * Si el token es válido, establece la autenticación en el contexto de Spring Security.
     * </p>
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException si ocurre un error de I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = obtenerTokenDeSolicitud(request);

            if (StringUtils.hasText(token)) {
                System.out.println("DEBUG: Token recibido para URI: " + request.getRequestURI());
                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.getUsernameFromJWT(token);
                    System.out.println("DEBUG: Token válido para usuario: " + username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("DEBUG: Autenticación establecida en SecurityContext para: " + username);
                    }
                } else {
                    System.err.println("DEBUG: La validación del token falló.");
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo establecer la autenticación del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT de la cabecera Authorization.
     * <p>
     * Busca una cabecera con formato: {@code Authorization: Bearer <token>}
     * y extrae el token removiendo el prefijo "Bearer ".
     * </p>
     *
     * @param request petición HTTP
     * @return token JWT si existe, null si no se encuentra o no tiene el formato correcto
     */
    private String obtenerTokenDeSolicitud(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}