package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.Repository.IncidenciasRepository;
import iesalonsocano.gestiondeaverias.Services.IncidenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de IncidenciasService.
 * Implementation of IncidenciasService.
 */
@Service
public class IncidenciasServiceImpl implements IncidenciasService {

    @Autowired
    private IncidenciasRepository incidenciasRepository;

    @Override
    public List<IncidenciasEntity> findAll() {
        // Retorna todas las incidencias. / Returns all incidents.
        return incidenciasRepository.findAll();
    }

    @Override
    public Optional<IncidenciasEntity> findById(Long id) {
        // Busca una incidencia por su identificador. / Finds an incident by its identifier.
        return incidenciasRepository.findById(id);
    }

    @Override
    public IncidenciasEntity save(IncidenciasEntity incidencia) {
        // 1. En tu entidad el campo es 'id', por eso usamos getId()
        if (incidencia.getId() == null) {
            // 2. Usamos uno de los estados de tu Enum (EN_ESPERA es tu valor por defecto)
            incidencia.setEstado(IncidenciasEntity.EstadoIncidencia.EN_PROGRESO);

            // 3. En tu entidad el campo es 'fechaReporte' (CamelCase), no 'fecha_reporte'
            incidencia.setFechaReporte(LocalDateTime.now());
        }
        return incidenciasRepository.save(incidencia);
    }

    @Override
    public void deleteById(Long id) {
        // Elimina la incidencia por ID. / Deletes the incident by ID.
        incidenciasRepository.deleteById(id);
    }

    // Filtros para Informes y Métricas
    @Override
    public List<IncidenciasEntity> findByEstado(IncidenciasEntity.EstadoIncidencia estado) {
        // Busca incidencias filtrando por el estado. / Finds incidents filtered by status.
        return incidenciasRepository.findByEstado(estado);
    }

    @Override
    public List<IncidenciasEntity> findByUsuarioId(Long usuarioId) {
        // Busca incidencias filtrando por el usuario. / Finds incidents filtered by user.
        return incidenciasRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<IncidenciasEntity> findByAulaId(Long aulaId) {
        // Busca incidencias filtrando por el aula. / Finds incidents filtered by classroom.
        return incidenciasRepository.findByAulaId(aulaId);
    }

    /**
     * Actualiza el estado de una incidencia.
     * <p>
     * Si el nuevo estado es RESUELTO, establece automáticamente la fecha de cierre.
     * </p>
     *
     * @param id identificador de la incidencia
     * @param nuevoEstado nuevo estado a establecer
     * @return incidencia actualizada
     * @throws RuntimeException si la incidencia no existe
     */
    public IncidenciasEntity actualizarEstado(Long id, IncidenciasEntity.EstadoIncidencia nuevoEstado) {
        IncidenciasEntity incidencia = incidenciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        incidencia.setEstado(nuevoEstado);

        // 1. Corregimos los nombres de los estados (Mayúsculas según tu Enum)
        // 2. Corregimos el nombre del campo (fechaCierre en lugar de fecha_cierre)
        if (nuevoEstado == IncidenciasEntity.EstadoIncidencia.RESUELTO) {
            incidencia.setFechaCierre(LocalDateTime.now());
        }

        return incidenciasRepository.save(incidencia);
    }
}