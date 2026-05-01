package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.Repository.ParteTrabajoRepository;
import iesalonsocano.gestiondeaverias.Services.ParteTrabajoService;
import iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Partes de Trabajo.
 */
@Service
public class ParteTrabajoServiceImpl implements ParteTrabajoService {

    @Autowired
    private ParteTrabajoRepository parteTrabajoRepository;

    @Override
    public List<ParteTrabajoEntity> findByIncidenciaId(Long incidenciaId) {
        return parteTrabajoRepository.findByIncidenciaIdWithTecnico(incidenciaId);
    }

    @Override
    public ParteTrabajoEntity save(ParteTrabajoEntity parte) {
        return parteTrabajoRepository.save(parte);
    }

    @Override
    public Optional<ParteTrabajoEntity> findById(Long id) {
        return parteTrabajoRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        parteTrabajoRepository.deleteById(id);
    }

    @Override
    public BigDecimal calcularTotalHoras(Long incidenciaId) {
        return parteTrabajoRepository.sumTiempoHorasByIncidenciaId(incidenciaId);
    }
}
