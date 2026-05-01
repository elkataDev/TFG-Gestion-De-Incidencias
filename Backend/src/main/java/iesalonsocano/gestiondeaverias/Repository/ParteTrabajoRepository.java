package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio JPA para la gestión de Partes de Trabajo.
 */
@Repository
public interface ParteTrabajoRepository extends JpaRepository<ParteTrabajoEntity, Long> {

    /**
     * Obtiene todos los partes de trabajo de una incidencia, con técnico precargado.
     */
    @Query("SELECT p FROM ParteTrabajoEntity p JOIN FETCH p.tecnico WHERE p.incidencia.id = :incidenciaId ORDER BY p.fechaRegistro ASC")
    List<ParteTrabajoEntity> findByIncidenciaIdWithTecnico(@Param("incidenciaId") Long incidenciaId);

    /**
     * Calcula el total de horas invertidas en una incidencia.
     */
    @Query("SELECT COALESCE(SUM(p.tiempoHoras), 0) FROM ParteTrabajoEntity p WHERE p.incidencia.id = :incidenciaId")
    BigDecimal sumTiempoHorasByIncidenciaId(@Param("incidenciaId") Long incidenciaId);

    /**
     * Obtiene los partes de trabajo de un técnico específico.
     */
    @Query("SELECT p FROM ParteTrabajoEntity p JOIN FETCH p.tecnico JOIN FETCH p.incidencia WHERE p.tecnico.id = :tecnicoId ORDER BY p.fechaRegistro DESC")
    List<ParteTrabajoEntity> findByTecnicoId(@Param("tecnicoId") Long tecnicoId);
}
