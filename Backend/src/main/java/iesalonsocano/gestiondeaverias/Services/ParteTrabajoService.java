package iesalonsocano.gestiondeaverias.Services;

import iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Contrato del servicio de Partes de Trabajo.
 */
public interface ParteTrabajoService {

    /** Obtiene todos los partes de trabajo de una incidencia. */
    List<ParteTrabajoEntity> findByIncidenciaId(Long incidenciaId);

    /** Guarda (crea o actualiza) un parte de trabajo. */
    ParteTrabajoEntity save(ParteTrabajoEntity parte);

    /** Busca un parte por su ID. */
    Optional<ParteTrabajoEntity> findById(Long id);

    /** Elimina un parte por su ID. */
    void deleteById(Long id);

    /** Calcula el total de horas invertidas en una incidencia. */
    BigDecimal calcularTotalHoras(Long incidenciaId);
}
