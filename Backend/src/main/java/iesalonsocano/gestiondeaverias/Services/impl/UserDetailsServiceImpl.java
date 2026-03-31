package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación de UserDetailsService para Spring Security.
 * <p>
 * Esta clase es utilizada por Spring Security para cargar los detalles del usuario
 * durante el proceso de autenticación.
 * </p>
 *
 * <p>
 * Convierte una entidad UsuariosEntity de la base de datos en un objeto UserDetails
 * que Spring Security puede utilizar para validación y autorización.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see UserDetailsService
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    /**
     * Carga un usuario por su nombre de usuario.
     * <p>
     * Este método es llamado automáticamente por Spring Security durante
     * el proceso de autenticación.
     * </p>
     *
     * @param username nombre de usuario a buscar
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuariosEntity usuario = usuariosRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getPassword())
                .disabled(!usuario.getActivo())
                .roles(String.valueOf(usuario.getRol()))
                .build();
    }
}