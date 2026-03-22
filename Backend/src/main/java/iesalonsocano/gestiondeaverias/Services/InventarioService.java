package iesalonsocano.gestiondeaverias.Services;

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import java.util.List;
import java.util.Optional;

/**
 * Define el contrato para las operaciones de Inventario.
 * Defines the contract for Inventory operations.
 */
public interface InventarioService {

    /**
     * Obtiene todos los artículos del inventario.
     * Gets all inventory items.
     */
    List<InventarioEntity> findAll();

    /**
     * Busca un artículo por su ID.
     * Finds an item by its ID.
     */
    Optional<InventarioEntity> findById(Long id);

    /**
     * Guarda (crea o actualiza) un artículo.
     * Saves (creates or updates) an item.
     */
    InventarioEntity save(InventarioEntity inventario);

    /**
     * Elimina un artículo por su ID.
     * Deletes an item by its ID.
     */
    void deleteById(Long id);

    /**
     * Busca artículos por su estado.
     *
     * @param estado estado del artículo
     * @return lista de artículos con el estado especificado
     */
    List<InventarioEntity> findByEstado(InventarioEntity.estadoInventario estado);

    /**
     * Busca todos los artículos ubicados en un aula.
     *
     * @param aulaId identificador del aula
     * @return lista de artículos del aula
     */
    List<InventarioEntity> findByAulaId(Long aulaId);

    /**
     * Busca un artículo por su código QR.
     *
     * @param codigo_QR código QR del artículo
     * @return artículo con el código QR especificado
     */
    InventarioEntity findByCodigo_QR(String codigo_QR);
}