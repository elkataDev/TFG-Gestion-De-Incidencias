package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialEstadoRepository extends JpaRepository<HistorialEstadoEntity, Long> {
    List<HistorialEstadoEntity> findByIncidenciaIdOrderByFechaCambioAsc(Long incidenciaId);
}
