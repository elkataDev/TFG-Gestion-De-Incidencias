package iesalonsocano.gestiondeaverias.services.impl; // Se recomienda usar un subpaquete 'impl'

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.repository.IncidenciasRepository;
import iesalonsocano.gestiondeaverias.service.IncidenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Guarda la incidencia. / Saves the incident.
        return incidenciasRepository.save(incidencia);
    }

    @Override
    public void deleteById(Long id) {
        // Elimina la incidencia por ID. / Deletes the incident by ID.
        incidenciasRepository.deleteById(id);
    }

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
}