package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.Repository.MovimientoInventarioRepository;
import iesalonsocano.gestiondeaverias.Services.MovimientoInventarioService;
import iesalonsocano.gestiondeaverias.entity.MovimientoInventarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de Movimientos de Inventario.
 */
@Service
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Override
    public List<MovimientoInventarioEntity> findByInventarioId(Long inventarioId) {
        return movimientoInventarioRepository.findByInventarioIdWithRelations(inventarioId);
    }

    @Override
    public MovimientoInventarioEntity save(MovimientoInventarioEntity movimiento) {
        return movimientoInventarioRepository.save(movimiento);
    }
}
