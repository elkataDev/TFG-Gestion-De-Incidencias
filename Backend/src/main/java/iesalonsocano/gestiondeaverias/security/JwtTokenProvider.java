package iesalonsocano.gestiondeaverias.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Proveedor de tokens JWT para autenticación.
 * <p>
 * Esta clase es responsable de:
 * <ul>
 *   <li>Generar tokens JWT cuando un usuario se autentica</li>
 *   <li>Validar tokens JWT en peticiones entrantes</li>
 *   <li>Extraer información del usuario desde el token</li>
 * </ul>
 * </p>
 *
 * <p>
 * Los tokens incluyen:
 * <ul>
 *   <li>Username (subject)</li>
 *   <li>Rol del usuario (claim "role")</li>
 *   <li>Fecha de emisión</li>
 *   <li>Fecha de expiración (24 horas por defecto)</li>
 * </ul>
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
@Component
public class JwtTokenProvider {

    /**
     * Clave secreta para firmar los tokens JWT.
     * <p>
     * Debe ser una cadena Base64 de al menos 256 bits para HS256.
     * </p>
     */
    private String secretKey = "VGhpcyBJcyBBIFZlcnkgU2VjcmV0IEtleSBUaGF0IFNob3VsZCBCZSBMb25nIEVub3VnaCAyNTY=";

    /**
     * Tiempo de expiración del token en milisegundos (24 horas).
     */
    private long jwtExpiration = 86400000;

    /**
     * Genera un token JWT para un usuario autenticado.
     * <p>
     * El token incluye el nombre de usuario como subject y el rol como claim adicional.
     * </p>
     *
     * @param authentication objeto de autenticación de Spring Security
     * @return token JWT firmado como String
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        // Extraemos el rol de la lista de autoridades
        String role = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .findFirst()
                .orElse("ROLE_usuario"); // Rol por defecto

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(username)
                .claim("role", role) // AÑADIMOS EL ROL AL TOKEN
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (username) del token JWT.
     *
     * @param token token JWT del que extraer el username
     * @return nombre de usuario contenido en el token
     * @throws JwtException si el token es inválido o está mal formado
     */
    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Valida un token JWT verificando su firma y que no haya expirado.
     *
     * @param authToken token JWT a validar
     * @return true si el token es válido, false si es inválido o ha expirado
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Aquí podrías loguear el error exacto: ex.getMessage()
            System.out.println("Token JWT inválido o expirado: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Decodifica la clave secreta Base64 y la convierte en una SecretKey.
     * <p>
     * Esta clave se utiliza para firmar y validar los tokens JWT con el algoritmo HS256.
     * </p>
     *
     * @return SecretKey para operaciones criptográficas
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}