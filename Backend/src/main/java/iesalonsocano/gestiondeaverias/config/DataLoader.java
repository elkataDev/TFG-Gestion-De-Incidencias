package iesalonsocano.gestiondeaverias.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iesalonsocano.gestiondeaverias.Repository.InventarioRepository;
import iesalonsocano.gestiondeaverias.Repository.UsuariosRepository; // <--- Nuevo
import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity; // <--- Nuevo
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder; // <--- Vital para encriptar

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(InventarioRepository inventarioRepository,
                                   UsuariosRepository usuariosRepository, // Inyectamos repo usuarios
                                   PasswordEncoder passwordEncoder) {     // Inyectamos encriptador
        return args -> {
            ObjectMapper mapper = new ObjectMapper();

            // ==========================================
            // 1. CARGA DE USUARIOS
            // ==========================================
            try (InputStream inputStream = new ClassPathResource("data/usuarios.json").getInputStream()) {
                TypeReference<List<UsuariosEntity>> typeRefUsers = new TypeReference<>() {};
                List<UsuariosEntity> usuariosList = mapper.readValue(inputStream, typeRefUsers);

                for (UsuariosEntity usuario : usuariosList) {
                    // Comprobamos si existe para no dar error de duplicado
                    Optional<UsuariosEntity> existe = usuariosRepository.findByNombreUsuario(usuario.getNombreUsuario());

                    if (existe.isEmpty()) {
                        // 🔐 ENCRIPTAMOS LA CONTRASEÑA ANTES DE GUARDAR
                        String passSinEncriptar = usuario.getPassword();
                        usuario.setPassword(passwordEncoder.encode(passSinEncriptar));

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
            // 2. CARGA DE INVENTARIO (Tu código existente)
            // ==========================================
            if (inventarioRepository.count() == 0) {
                try (InputStream inputStream = new ClassPathResource("data/inventario.json").getInputStream()) {
                    TypeReference<List<InventarioEntity>> typeRefInv = new TypeReference<>() {};
                    List<InventarioEntity> inventarioList = mapper.readValue(inputStream, typeRefInv);

                    // Lógica para evitar duplicar Aulas
                    Map<String, AulasEntity> aulasCache = new HashMap<>();

                    for (InventarioEntity item : inventarioList) {
                        // Asegúrate de que tu InventarioEntity tiene el campo 'aula' como Objeto, no String
                        // Si lo cambiaste a String en el paso anterior, esta parte del mapa sobra.
                        // Si sigues usando AulasEntity, mantén esto:
                        if (item.getAula() != null) {
                            String nombreAula = item.getAula().getNombre();
                            if (aulasCache.containsKey(nombreAula)) {
                                item.setAula(aulasCache.get(nombreAula));
                            } else {
                                aulasCache.put(nombreAula, item.getAula());
                            }
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
        };
    }
}