package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HistorialEstadoRepository extends JpaRepository<HistorialEstadoEntity, Long> {
    List<HistorialEstadoEntity> findByIncidenciaIdOrderByFechaCambioAsc(Long incidenciaId);

    /**
     * Busca historial por incidencia con usuario precargado (JOIN FETCH).
     */
    @Query("SELECT h FROM HistorialEstadoEntity h LEFT JOIN FETCH h.usuario WHERE h.incidencia.id = :incidenciaId ORDER BY h.fechaCambio ASC")
    List<HistorialEstadoEntity> findByIncidenciaIdWithUsuario(@Param("incidenciaId") Long incidenciaId);

    void deleteByIncidenciaId(Long incidenciaId);

    /** Borra historial de una lista de incidencias (al eliminar usuario). */
    void deleteByIncidenciaIdIn(List<Long> incidenciaIds);

    /** Desvincula el usuario del historial sin borrar el registro (usuario_id es nullable). */
    @Modifying
    @Query("UPDATE HistorialEstadoEntity h SET h.usuario = null WHERE h.usuario.id = :usuarioId")
    void clearUsuarioFromHistorial(@Param("usuarioId") Long usuarioId);
}
