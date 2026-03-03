package iesalonsocano.gestiondeaverias.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {

    // --- INYECCIÓN DE VALORES DESDE APPLICATION.YML ---

    // @Value("${security.jwt.secret-key}")
    private String secretKey = "VGhpcyBJcyBBIFZlcnkgU2VjcmV0IEtleSBUaGF0IFNob3VsZCBCZSBMb25nIEVub3VnaCAyNTY=";

    // Y si tiene expiración, también:
    // @Value("${security.jwt.expiration-time}")
    private long jwtExpiration = 86400000;

    // --- 1. Generación del Token ---

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                // Aquí usamos la variable inyectada 'expirationTime'
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    // --- 2. Validación y Extracción de datos ---

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // --- 3. Métodos Auxiliares (Privados) ---

    private SecretKey getSignInKey() {
        // Aquí usamos la variable inyectada 'secretKey'
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}