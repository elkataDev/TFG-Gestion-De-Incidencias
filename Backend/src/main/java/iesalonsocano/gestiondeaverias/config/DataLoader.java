package iesalonsocano.gestiondeaverias.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import iesalonsocano.gestiondeaverias.Repository.AulasRepository;
import iesalonsocano.gestiondeaverias.Repository.IncidenciasRepository;
import iesalonsocano.gestiondeaverias.Repository.InventarioRepository;
import iesalonsocano.gestiondeaverias.Repository.UsuariosRepository;
import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Configuración para la carga inicial de datos en la base de datos.
 * <p>
 * Esta clase se encarga de cargar datos de prueba desde archivos JSON ubicados
 * en el directorio {@code resources/data/} al iniciar la aplicación.
 * </p>
 *
 * <p>
 * Los datos cargados incluyen:
 * <ul>
 *   <li>Usuarios del sistema con contraseñas encriptadas</li>
 *   <li>Inventario de equipos informáticos</li>
 *   <li>Incidencias técnicas de ejemplo</li>
 * </ul>
 * </p>
 *
 * <p>
 * La carga es idempotente: si los datos ya existen, no se duplican.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see CommandLineRunner
 */
@Configuration
public class DataLoader {

    /**
     * Bean que inicializa la base de datos con datos de prueba al arrancar la aplicación.
     * <p>
     * Este método se ejecuta automáticamente después de que el contexto de Spring
     * ha sido completamente inicializado.
     * </p>
     *
     * <p>
     * Secuencia de carga:
     * <ol>
     *   <li>Usuarios - desde {@code data/usuarios.json}</li>
     *   <li>Inventario - desde {@code data/inventario.json}</li>
     *   <li>Incidencias - desde {@code data/incidencias.json}</li>
     * </ol>
     * </p>
     *
     * @param inventarioRepository repositorio para gestionar el inventario
     * @param usuariosRepository repositorio para gestionar usuarios
     * @param aulasRepository repositorio para gestionar aulas
     * @param incidenciasRepository repositorio para gestionar incidencias
     * @param passwordEncoder codificador de contraseñas para seguridad
     * @return CommandLineRunner que ejecuta la lógica de carga
     */
    @Bean
    CommandLineRunner initDatabase(
            InventarioRepository inventarioRepository,
            UsuariosRepository usuariosRepository,
            AulasRepository aulasRepository,
            IncidenciasRepository incidenciasRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // ==========================================
            // 1. CARGA DE USUARIOS
            // ==========================================
            try (InputStream inputStream = new ClassPathResource("data/usuarios.json").getInputStream()) {
                TypeReference<List<UsuariosEntity>> typeRefUsers = new TypeReference<>() {};
                List<UsuariosEntity> usuariosList = mapper.readValue(inputStream, typeRefUsers);

                for (UsuariosEntity usuario : usuariosList) {
                    Optional<UsuariosEntity> existe = usuariosRepository.findByNombreUsuario(usuario.getNombreUsuario());
                    if (existe.isEmpty()) {
                        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                        usuariosRepository.save(usuario);
                        System.out.println("👤 Usuario creado: " + usuario.getNombreUsuario());
                    } else {
                        System.out.println("ℹ️ Usuario " + usuario.getNombreUsuario() + " ya existe. Saltando.");
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Error al cargar usuarios: " + e.getMessage());
            }

            // ==========================================
            // 2. CARGA DE INVENTARIO
            // ==========================================
            if (inventarioRepository.count() == 0) {
                try (InputStream inputStream = new ClassPathResource("data/inventario.json").getInputStream()) {
                    TypeReference<List<InventarioEntity>> typeRefInv = new TypeReference<>() {};
                    List<InventarioEntity> inventarioList = mapper.readValue(inputStream, typeRefInv);

                    Map<String, AulasEntity> aulasCache = new HashMap<>();
                    for (InventarioEntity item : inventarioList) {
                        if (item.getAula() != null) {
                            String nombreAula = item.getAula().getNombre();
                            AulasEntity aula = aulasCache.get(nombreAula);
                            if (aula == null) {
                                // Revisa si existe en DB
                                Optional<AulasEntity> aulaDB = aulasRepository.findByNombre(nombreAula);
                                aula = aulaDB.orElse(item.getAula());
                                aulasCache.put(nombreAula, aula);
                            }
                            item.setAula(aula);
                        }
                    }
                    inventarioRepository.saveAll(inventarioList);
                    System.out.println("✅ Inventario cargado correctamente.");
                } catch (Exception e) {
                    System.err.println("❌ Error al cargar inventario: " + e.getMessage());
                }
            } else {
                System.out.println("ℹ️ El inventario ya tiene datos.");
            }

            // ==========================================
            // 3. CARGA DE INCIDENCIAS
            // ==========================================
            if (incidenciasRepository.count() == 0) {
                try (InputStream inputStream = new ClassPathResource("data/incidencias.json").getInputStream()) {
                    TypeReference<List<IncidenciasEntity>> typeRefInc = new TypeReference<>() {};
                    List<IncidenciasEntity> incidenciasList = mapper.readValue(inputStream, typeRefInc);

                    for (IncidenciasEntity inc : incidenciasList) {
                        // Mapear usuario
                        if (inc.getUsuario() != null) {
                            String nombreUsuario = inc.getUsuario().getNombreUsuario();
                            Optional<UsuariosEntity> usuarioDB = usuariosRepository.findByNombreUsuario(nombreUsuario);
                            usuarioDB.ifPresent(inc::setUsuario);
                        }
                        // Mapear aula
                        if (inc.getAula() != null) {
                            String nombreAula = inc.getAula().getNombre();
                            Optional<AulasEntity> aulaDB = aulasRepository.findByNombre(nombreAula);
                            aulaDB.ifPresent(inc::setAula);
                        }
                    }

                    incidenciasRepository.saveAll(incidenciasList);
                    System.out.println("✅ Incidencias cargadas correctamente.");
                } catch (Exception e) {
                    System.err.println("❌ Error al cargar incidencias: " + e.getMessage());
                }
            } else {
                System.out.println("ℹ️ Las incidencias ya tienen datos.");
            }
        };
    }
}
