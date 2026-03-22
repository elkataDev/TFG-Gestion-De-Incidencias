package iesalonsocano.gestiondeaverias.Services;

import iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import java.util.List;

public interface HistorialEstadoService {
    HistorialEstadoEntity save(HistorialEstadoEntity historial);
    HistorialEstadoEntity registrarCambio(IncidenciasEntity incidencia, IncidenciasEntity.EstadoIncidencia estadoAnterior, IncidenciasEntity.EstadoIncidencia estadoNuevo, UsuariosEntity usuario);
    List<HistorialEstadoEntity> findByIncidenciaId(Long incidenciaId);
}
