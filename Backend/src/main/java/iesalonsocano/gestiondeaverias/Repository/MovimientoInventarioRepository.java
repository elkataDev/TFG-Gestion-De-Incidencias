package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.MovimientoInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la gestión del historial de movimientos de inventario.
 */
@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventarioEntity, Long> {

    /**
     * Obtiene todos los movimientos de un activo específico, ordenados por fecha.
     */
    @Query("SELECT m FROM MovimientoInventarioEntity m " +
           "LEFT JOIN FETCH m.aulaOrigen LEFT JOIN FETCH m.aulaDestino " +
           "JOIN FETCH m.responsable " +
           "WHERE m.inventario.id = :inventarioId ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventarioEntity> findByInventarioIdWithRelations(@Param("inventarioId") Long inventarioId);

    /**
     * Obtiene todos los movimientos realizados por un responsable.
     */
    @Query("SELECT m FROM MovimientoInventarioEntity m " +
           "JOIN FETCH m.inventario LEFT JOIN FETCH m.aulaOrigen LEFT JOIN FETCH m.aulaDestino " +
           "JOIN FETCH m.responsable " +
           "WHERE m.responsable.id = :responsableId ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventarioEntity> findByResponsableId(@Param("responsableId") Long responsableId);
}
