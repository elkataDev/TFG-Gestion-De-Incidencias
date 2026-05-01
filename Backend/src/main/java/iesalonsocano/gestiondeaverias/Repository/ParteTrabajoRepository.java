package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParteTrabajoRepository extends JpaRepository<ParteTrabajoEntity, Long> {

    @Query("SELECT p FROM ParteTrabajoEntity p LEFT JOIN FETCH p.tecnico WHERE p.incidencia.id = :incidenciaId ORDER BY p.fechaParte DESC")
    List<ParteTrabajoEntity> findByIncidenciaId(@Param("incidenciaId") Long incidenciaId);
}
