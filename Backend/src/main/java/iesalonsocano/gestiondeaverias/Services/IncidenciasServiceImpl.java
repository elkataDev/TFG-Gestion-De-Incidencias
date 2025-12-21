<<<<<<< Updated upstream:Backend/src/main/java/iesalonsocano/gestiondeaverias/Services/IncidenciasServiceImpl.java
package iesalonsocano.gestiondeaverias.service.impl; // Se recomienda usar un subpaquete 'impl'
=======
package iesalonsocano.gestiondeaverias.Services.impl;
>>>>>>> Stashed changes:Backend/src/main/java/iesalonsocano/gestiondeaverias/Services/impl/IncidenciasServiceImpl.java

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.Repository.IncidenciasRepository;
import iesalonsocano.gestiondeaverias.Services.IncidenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenciasServiceImpl implements IncidenciasService {

    @Autowired
    private IncidenciasRepository incidenciasRepository;

    @Override
    public List<IncidenciasEntity> findAll() {
        return incidenciasRepository.findAll();
    }

    @Override
    public Optional<IncidenciasEntity> findById(Long id) {
        return incidenciasRepository.findById(id);
    }

    @Override
    public IncidenciasEntity save(IncidenciasEntity incidencia) {
        // 1. En tu entidad el campo es 'id', por eso usamos getId()
        if (incidencia.getId() == null) {
            // 2. Usamos uno de los estados de tu Enum (EN_ESPERA es tu valor por defecto)
            incidencia.setEstado(IncidenciasEntity.EstadoIncidencia.EN_ESPERA);

            // 3. En tu entidad el campo es 'fechaReporte' (CamelCase), no 'fecha_reporte'
            incidencia.setFechaReporte(LocalDateTime.now());
        }
        return incidenciasRepository.save(incidencia);
    }

    @Override
    public void deleteById(Long id) {
        incidenciasRepository.deleteById(id);
    }

    // Filtros para Informes y Métricas
    @Override
    public List<IncidenciasEntity> findByEstado(IncidenciasEntity.EstadoIncidencia estado) {
        return incidenciasRepository.findByEstado(estado);
    }

    @Override
    public List<IncidenciasEntity> findByUsuarioId(Long usuarioId) {
        return incidenciasRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<IncidenciasEntity> findByAulaId(Long aulaId) {
        return incidenciasRepository.findByAulaId(aulaId);
    }

    // Lógica de Flujo de Estados
    public IncidenciasEntity actualizarEstado(Long id, IncidenciasEntity.EstadoIncidencia nuevoEstado) {
        IncidenciasEntity incidencia = incidenciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        incidencia.setEstado(nuevoEstado);

        // 1. Corregimos los nombres de los estados (Mayúsculas según tu Enum)
        // 2. Corregimos el nombre del campo (fechaCierre en lugar de fecha_cierre)
        if (nuevoEstado == IncidenciasEntity.EstadoIncidencia.RESUELTO ||
                nuevoEstado == IncidenciasEntity.EstadoIncidencia.CERRADO) {
            incidencia.setFechaCierre(LocalDateTime.now());
        }

        return incidenciasRepository.save(incidencia);
    }
}