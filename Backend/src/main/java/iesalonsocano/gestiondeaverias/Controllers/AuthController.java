package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.LoginResponse;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.security.JwtTokenProvider;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la autenticación y registro de usuarios.
 * <p>
 * Proporciona endpoints para:
 * <ul>
 *   <li>Login con JWT (JSON Web Token)</li>
 *   <li>Registro de nuevos usuarios</li>
 * </ul>
 * </p>
 *
 * <p>
 * El endpoint de login devuelve un token JWT que debe incluirse en las
 * cabeceras de las siguientes peticiones como: {@code Authorization: Bearer <token>}
 * </p>
 *
 * <p>
 * Base URL: {@code /api/auth}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see JwtTokenProvider
 * @see LoginResponse
 */
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UsuariosService usuariosService;

    /**
     * Autentica a un usuario y genera un token JWT.
     * <p>
     * Proceso:
     * <ol>
     *   <li>Valida las credenciales (usuario y contraseña)</li>
     *   <li>Genera un token JWT si la autenticación es exitosa</li>
     *   <li>Recupera el rol del usuario desde la base de datos</li>
     *   <li>Devuelve el token junto con el nombre de usuario y rol</li>
     * </ol>
     * </p>
     *
     * @param loginRequest objeto con username y password
     * @return ResponseEntity con LoginResponse conteniendo token, username y rol
     * @throws BadCredentialsException si las credenciales son incorrectas
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // 1. Crear el "sobre" con la información cruda
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. Guardar la sesión en memoria (si pasó el paso 2)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 2. Generar el Token
        String token = tokenProvider.generateToken(authentication);

        // 3. RECUPERAR USUARIO DE LA BASE DE DATOS
        // Usamos el servicio que acabamos de implementar.
        // Si la autenticación pasó, el usuario EXISTE seguro, pero usamos orElseThrow por seguridad.
        UsuariosEntity usuarioDb = usuariosService.findByNombreUsuario(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado en base de datos tras autenticación."));

        // 4. Obtener el rol real de la entidad
        // IMPORTANTE: Asegúrate de que tu UsuariosEntity tenga el metodo getRol() o getRole()
        String role = usuarioDb.getRol(); // O usuarioDb.getRole().toString() si es un Enum

        // 5. Devolver respuesta
        return ResponseEntity.ok(new LoginResponse(token, usuarioDb.getNombreUsuario(), role));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * <p>
     * La contraseña será encriptada automáticamente por el servicio de usuarios.
     * </p>
     *
     * @param usuario entidad con los datos del usuario a registrar
     * @return ResponseEntity con el usuario creado (sin contraseña visible)
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody UsuariosEntity usuario) {

        UsuariosEntity guardado = usuariosService.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Usuario registrado correctamente",
                        "id", guardado.getId(),
                        "username", guardado.getNombreUsuario(),
                        "email", guardado.getEmail()
                )
        );
    }
}

/**
 * Clase interna que representa la petición de login.
 * <p>
 * Contiene las credenciales del usuario para autenticación.
 * </p>
 */


class LoginRequest {
    private String username;
    private String password;

    /**
     * Obtiene el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsername() { return username; }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nombre de usuario
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Obtiene la contraseña.
     *
     * @return contraseña
     */
    public String getPassword() { return password; }

    /**
     * Establece la contraseña.
     *
     * @param password contraseña
     */
    public void setPassword(String password) { this.password = password; }
}