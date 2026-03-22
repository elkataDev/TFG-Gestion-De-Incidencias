package iesalonsocano.gestiondeaverias.DTO;

/**
 * DTO para la respuesta del endpoint de login.
 * <p>
 * Contiene el token JWT generado y la información básica del usuario
 * autenticado para ser utilizada por el cliente (frontend).
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
public class LoginResponse {
    /**
     * Token JWT para autenticar peticiones futuras.
     */
    private String token;

    /**
     * Nombre de usuario autenticado.
     */
    private String username;

    /**
     * Rol del usuario (USER, ADMIN, TECNICO).
     */
    private String role;

    /**
     * Constructor con todos los campos.
     *
     * @param token token JWT generado
     * @param username nombre del usuario
     * @param role rol del usuario
     */
    public LoginResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    /**
     * Obtiene el token JWT.
     *
     * @return token JWT
     */
    public String getToken() {
        return token;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return rol del usuario
     */
    public String getRole() {
        return role;
    }
}
