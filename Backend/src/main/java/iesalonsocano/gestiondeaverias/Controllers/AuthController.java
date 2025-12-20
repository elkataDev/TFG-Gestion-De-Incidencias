package iesalonsocano.gestiondeaverias.controllers;

import iesalonsocano.gestiondeaverias.DTO.LoginResponse;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.security.JwtTokenProvider;
import iesalonsocano.gestiondeaverias.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UsuariosService usuariosService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // 1. Validar credenciales con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
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

    // REGISTRO
    @PostMapping("/registro")
    public ResponseEntity<UsuariosEntity> registrar(@RequestBody UsuariosEntity usuario) {
        return ResponseEntity.ok(usuariosService.save(usuario));
    }
}

class LoginRequest {
    private String username;
    private String password;
    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}