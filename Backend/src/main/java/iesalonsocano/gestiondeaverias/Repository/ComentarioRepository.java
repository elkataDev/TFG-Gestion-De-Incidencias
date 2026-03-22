package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {
    List<ComentarioEntity> findByIncidenciaIdOrderByFechaAsc(Long incidenciaId);
}
