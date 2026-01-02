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

@Component
public class JwtTokenProvider {

    // Inyectamos los valores desde application.yml
// @Value("${security.jwt.secret-key}")
    private String secretKey = "VGhpcyBJcyBBIFZlcnkgU2VjcmV0IEtleSBUaGF0IFNob3VsZCBCZSBMb25nIEVub3VnaCAyNTY=";

    // @Value("${security.jwt.expiration-time}")
    private long jwtExpiration = 86400000;

    /**
     * Genera el token JWT
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        // Extraemos el rol de la lista de autoridades
        String role = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .findFirst()
                .orElse("USUARIO"); // Rol por defecto

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
     * Obtiene el username del token
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
     * Valida el token
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
     * Metodo auxiliar para decodificar la clave secreta
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}