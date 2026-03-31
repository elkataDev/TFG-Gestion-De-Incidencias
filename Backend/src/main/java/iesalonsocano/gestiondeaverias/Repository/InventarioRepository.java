package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity.EstadoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de persistencia del inventario.
 * <p>
 * Proporciona métodos de consulta para filtrar artículos por estado,
 * aula, nombre o código QR.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see InventarioEntity
 */
@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {

    /**
     * Busca artículos del inventario por su estado.
     *
     * @param estado estado del artículo (DISPONIBLE, EN_USO, DANADO)
     * @return lista de artículos con el estado especificado
     */
    List<InventarioEntity> findByEstado(EstadoInventario estado);

    /**
     * Obtiene todos los artículos con su aula precargada (JOIN FETCH).
     * Evita LazyInitializationException al acceder a la relación aula.
     *
     * @return lista de artículos con el aula cargada
     */
    @Query("SELECT i FROM InventarioEntity i LEFT JOIN FETCH i.aula")
    List<InventarioEntity> findAllWithAula();

    /**
     * Obtiene un artículo por ID con su aula precargada (JOIN FETCH).
     *
     * @param id identificador del artículo
     * @return artículo con el aula cargada
     */
    @Query("SELECT i FROM InventarioEntity i LEFT JOIN FETCH i.aula WHERE i.id = :id")
    Optional<InventarioEntity> findByIdWithAula(Long id);

    /**
     * Busca todos los artículos ubicados en un aula específica.
     *
     * @param aulaId identificador del aula
     * @return lista de artículos del aula
     */
    List<InventarioEntity> findByAulaId(Long aulaId);

    /**
     * Busca artículos cuyo nombre contenga el texto especificado (búsqueda sin distinguir mayúsculas/minúsculas).
     * <p>
     * Útil para implementar funcionalidad de búsqueda en la UI.
     * </p>
     *
     * @param nombre texto a buscar en el nombre
     * @return lista de artículos que contienen el texto en su nombre
     */
    List<InventarioEntity> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca un artículo por su código QR único.
     * <p>
     * Utilizado para escaneo rápido y gestión de inventario.
     * </p>
     *
     * @param codigo_QR código QR del artículo
     * @return artículo con el código QR especificado, o null si no existe
     */
    InventarioEntity findByCodigoQR(String codigo_QR);
}