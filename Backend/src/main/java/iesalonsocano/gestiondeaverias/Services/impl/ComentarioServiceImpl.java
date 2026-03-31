package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.Repository.ComentarioRepository;
import iesalonsocano.gestiondeaverias.Services.ComentarioService;
import iesalonsocano.gestiondeaverias.entity.ComentarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public ComentarioEntity save(ComentarioEntity comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    public List<ComentarioEntity> findByIncidenciaId(Long incidenciaId) {
        // Usa JOIN FETCH para evitar LazyInitializationException
        return comentarioRepository.findByIncidenciaIdWithUsuario(incidenciaId);
    }
}
