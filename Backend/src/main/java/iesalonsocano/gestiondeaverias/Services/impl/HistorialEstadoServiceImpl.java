package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.Repository.HistorialEstadoRepository;
import iesalonsocano.gestiondeaverias.Services.HistorialEstadoService;
import iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialEstadoServiceImpl implements HistorialEstadoService {

    @Autowired
    private HistorialEstadoRepository repository;

    @Override
    public HistorialEstadoEntity save(HistorialEstadoEntity historial) {
        return repository.save(historial);
    }

    @Override
    public HistorialEstadoEntity registrarCambio(IncidenciasEntity incidencia, IncidenciasEntity.EstadoIncidencia estadoAnterior, IncidenciasEntity.EstadoIncidencia estadoNuevo, UsuariosEntity usuario) {
        HistorialEstadoEntity historial = new HistorialEstadoEntity();
        historial.setIncidencia(incidencia);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setUsuario(usuario);
        return repository.save(historial);
    }

    @Override
    public List<HistorialEstadoEntity> findByIncidenciaId(Long incidenciaId) {
        // Usa JOIN FETCH para evitar LazyInitializationException
        return repository.findByIncidenciaIdWithUsuario(incidenciaId);
    }

    @Override
    public List<HistorialEstadoEntity> findAll() {
        return repository.findAllWithRelations();
    }
}
