package iesalonsocano.gestiondeaverias.service;

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
     * Busca artículos por estado.
     * Finds items by status.
     */
    List<InventarioEntity> findByEstado(InventarioEntity.estadoInventario estado);

    /**
     * Busca artículos por ID de aula.
     * Finds items by classroom ID.
     */
    List<InventarioEntity> findByAulaId(Long aulaId);

    /**
     * Busca un artículo por código QR.
     * Finds an item by QR code.
     */
    InventarioEntity findByCodigo_QR(String codigo_QR);
}