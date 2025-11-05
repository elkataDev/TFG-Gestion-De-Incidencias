package iesalonsocano.gestiondeaverias.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.repository.InventarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class DataLoader {

    /*Este archivo carga datos desde un JSON al iniciar tu aplicación.
    *
    * This file loads data from a JSON when your application starts.*/

    @Bean
    CommandLineRunner initDatabase(InventarioRepository inventarioRepository) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<InventarioEntity>> typeReference = new TypeReference<>() {};

            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/inventario.json")) {
                if (inputStream != null) {
                    List<InventarioEntity> inventarioList = mapper.readValue(inputStream, typeReference);
                    inventarioRepository.saveAll(inventarioList);
                    System.out.println("✅ Datos de inventario cargados correctamente (" + inventarioList.size() + " elementos).");
                } else {
                    System.out.println("⚠️ No se encontró el archivo inventario.json en /resources/data/");
                }
            } catch (Exception e) {
                System.err.println("❌ Error al cargar inventario: " + e.getMessage());
            }
        };
    }
}