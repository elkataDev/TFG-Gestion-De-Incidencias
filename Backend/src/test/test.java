
import iesalonsocano.gestiondeaverias.controller.UsuariosController;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.repository.UsuariosRepository;
import iesalonsocano.gestiondeaverias.service.UsuariosService;
import iesalonsocano.gestiondeaverias.service.impl.UsuariosServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuariosServiceImplTest {

    @Mock
    private UsuariosRepository usuariosRepository;

    @InjectMocks
    private UsuariosServiceImpl usuariosService;

    @Test
    void findAll_debeDevolverListaDeUsuarios() {
        List<UsuariosEntity> esperados = List.of(
                UsuariosEntity.builder().id(1L).nombreUsuario("ana").email("ana@test.com").password("1234").activo(true).build(),
                UsuariosEntity.builder().id(2L).nombreUsuario("luis").email("luis@test.com").password("abcd").activo(true).build()
        );

        when(usuariosRepository.findAll()).thenReturn(esperados);

        List<UsuariosEntity> resultado = usuariosService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("ana", resultado.get(0).getNombreUsuario());
        verify(usuariosRepository, times(1)).findAll();
    }

    @Test
    void findById_debeDelegarEnRepositorio() {
        UsuariosEntity usuario = UsuariosEntity.builder().id(8L).nombreUsuario("maria").email("maria@test.com").password("pw").activo(true).build();
        when(usuariosRepository.findById(8L)).thenReturn(Optional.of(usuario));

        Optional<UsuariosEntity> resultado = usuariosService.findById(8L);

        assertTrue(resultado.isPresent());
        assertEquals("maria", resultado.get().getNombreUsuario());
        verify(usuariosRepository).findById(8L);
    }

    @Test
    void save_debeGuardarUsuarioSinModificarPassword() {
        UsuariosEntity usuario = UsuariosEntity.builder().nombreUsuario("eva").email("eva@test.com").password("textoPlano").activo(true).build();
        when(usuariosRepository.save(usuario)).thenReturn(usuario);

        UsuariosEntity guardado = usuariosService.save(usuario);

        assertEquals("textoPlano", guardado.getPassword());
        verify(usuariosRepository).save(usuario);
    }

    @Test
    void deleteById_debeInvocarRepositorio() {
        usuariosService.deleteById(5L);
        verify(usuariosRepository).deleteById(5L);
    }

    @Test
    void findByNombreUsuario_debeDelegarEnRepositorio() {
        UsuariosEntity usuario = UsuariosEntity.builder().id(3L).nombreUsuario("pepe").email("pepe@test.com").password("pw").activo(true).build();
        when(usuariosRepository.findByNombreUsuario("pepe")).thenReturn(Optional.of(usuario));

        Optional<UsuariosEntity> resultado = usuariosService.findByNombreUsuario("pepe");

        assertTrue(resultado.isPresent());
        assertEquals(3L, resultado.get().getId());
        verify(usuariosRepository).findByNombreUsuario("pepe");
    }

    @Test
    void findByEmail_debeDelegarEnRepositorio() {
        UsuariosEntity usuario = UsuariosEntity.builder().id(4L).nombreUsuario("sara").email("sara@test.com").password("pw").activo(true).build();
        when(usuariosRepository.findByEmail("sara@test.com")).thenReturn(Optional.of(usuario));

        Optional<UsuariosEntity> resultado = usuariosService.findByEmail("sara@test.com");

        assertTrue(resultado.isPresent());
        assertEquals("sara", resultado.get().getNombreUsuario());
        verify(usuariosRepository).findByEmail("sara@test.com");
    }

    @Test
    void findByActivoTrue_debeDelegarEnRepositorio() {
        List<UsuariosEntity> activos = List.of(
                UsuariosEntity.builder().id(1L).nombreUsuario("u1").email("u1@test.com").password("pw").activo(true).build()
        );
        when(usuariosRepository.findByActivoTrue()).thenReturn(activos);

        List<UsuariosEntity> resultado = usuariosService.findByActivoTrue();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
        verify(usuariosRepository).findByActivoTrue();
    }
}

@ExtendWith(MockitoExtension.class)
class UsuariosControllerTest {

    @Mock
    private UsuariosService usuariosService;

    @InjectMocks
    private UsuariosController usuariosController;

    @Test
    void getAllUsuarios_debeDevolverLista() {
        List<UsuariosEntity> usuarios = List.of(
                UsuariosEntity.builder().id(1L).nombreUsuario("ana").email("ana@test.com").password("123").activo(true).build()
        );
        when(usuariosService.findAll()).thenReturn(usuarios);

        List<UsuariosEntity> resultado = usuariosController.getAllUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("ana", resultado.get(0).getNombreUsuario());
        verify(usuariosService).findAll();
    }

    @Test
    void getUsuarioById_cuandoExiste_debeResponder200() {
        UsuariosEntity usuario = UsuariosEntity.builder().id(10L).nombreUsuario("ana").email("ana@test.com").password("pw").activo(true).build();
        when(usuariosService.findById(10L)).thenReturn(Optional.of(usuario));

        ResponseEntity<UsuariosEntity> response = usuariosController.getUsuarioById(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());
        verify(usuariosService).findById(10L);
    }

    @Test
    void getUsuarioById_cuandoNoExiste_debeResponder404() {
        when(usuariosService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<UsuariosEntity> response = usuariosController.getUsuarioById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuariosService).findById(99L);
    }

    @Test
    void createUsuario_debeResponder201YGuardar() {
        UsuariosEntity entrada = UsuariosEntity.builder().nombreUsuario("nuevo").email("nuevo@test.com").password("pw").activo(true).build();
        UsuariosEntity guardado = UsuariosEntity.builder().id(11L).nombreUsuario("nuevo").email("nuevo@test.com").password("pw").activo(true).build();

        when(usuariosService.save(entrada)).thenReturn(guardado);

        ResponseEntity<UsuariosEntity> response = usuariosController.createUsuario(entrada);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(11L, response.getBody().getId());
        verify(usuariosService).save(entrada);
    }

    @Test
    void updateUsuario_cuandoExiste_debeActualizarYResponder200() {
        UsuariosEntity existente = UsuariosEntity.builder().id(5L).nombreUsuario("old").email("old@test.com").password("old").activo(true).build();
        UsuariosEntity cambios = UsuariosEntity.builder().nombreUsuario("new").email("new@test.com").password("newpass").activo(false).build();
        UsuariosEntity actualizado = UsuariosEntity.builder().id(5L).nombreUsuario("new").email("new@test.com").password("newpass").activo(false).build();

        when(usuariosService.findById(5L)).thenReturn(Optional.of(existente));
        when(usuariosService.save(any(UsuariosEntity.class))).thenReturn(actualizado);

        ResponseEntity<UsuariosEntity> response = usuariosController.updateUsuario(5L, cambios);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("new", response.getBody().getNombreUsuario());
        verify(usuariosService).findById(5L);
        verify(usuariosService).save(any(UsuariosEntity.class));
    }

    @Test
    void updateUsuario_cuandoNoExiste_debeResponder404() {
        UsuariosEntity cambios = UsuariosEntity.builder().nombreUsuario("x").email("x@test.com").password("x").activo(true).build();
        when(usuariosService.findById(777L)).thenReturn(Optional.empty());

        ResponseEntity<UsuariosEntity> response = usuariosController.updateUsuario(777L, cambios);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuariosService).findById(777L);
        verify(usuariosService, never()).save(any(UsuariosEntity.class));
    }

    @Test
    void deleteUsuario_cuandoExiste_debeResponder204() {
        UsuariosEntity existente = UsuariosEntity.builder().id(6L).nombreUsuario("borrar").email("borrar@test.com").password("pw").activo(true).build();
        when(usuariosService.findById(6L)).thenReturn(Optional.of(existente));

        ResponseEntity<Void> response = usuariosController.deleteUsuario(6L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuariosService).findById(6L);
        verify(usuariosService).deleteById(6L);
    }

    @Test
    void deleteUsuario_cuandoNoExiste_debeResponder404() {
        when(usuariosService.findById(404L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = usuariosController.deleteUsuario(404L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuariosService).findById(404L);
        verify(usuariosService, never()).deleteById(any(Long.class));
    }
}
