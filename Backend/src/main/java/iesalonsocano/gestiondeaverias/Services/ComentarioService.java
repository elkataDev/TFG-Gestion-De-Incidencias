package iesalonsocano.gestiondeaverias.Services;

import iesalonsocano.gestiondeaverias.entity.ComentarioEntity;
import java.util.List;

public interface ComentarioService {
    ComentarioEntity save(ComentarioEntity comentario);
    List<ComentarioEntity> findByIncidenciaId(Long incidenciaId);
}
