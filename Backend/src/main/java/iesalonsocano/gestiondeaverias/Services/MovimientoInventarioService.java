package iesalonsocano.gestiondeaverias.Services;

import iesalonsocano.gestiondeaverias.entity.MovimientoInventarioEntity;

import java.util.List;

/**
 * Contrato del servicio de Movimientos de Inventario.
 */
public interface MovimientoInventarioService {

    /** Obtiene todos los movimientos de un activo. */
    List<MovimientoInventarioEntity> findByInventarioId(Long inventarioId);

    /** Guarda un nuevo movimiento. */
    MovimientoInventarioEntity save(MovimientoInventarioEntity movimiento);
}
