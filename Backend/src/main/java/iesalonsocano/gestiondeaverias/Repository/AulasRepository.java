package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AulasRepository extends JpaRepository<AulasEntity, Long> {

    // Custom query methods / Métodos de consulta personalizados
    Optional<AulasEntity> findByNombre(String nombre);

    // Checks if a classroom with the given name already exists
    // Verifica si ya existe un aula con el nombre dado
    boolean existsByNombre(String nombre);
}
