package iesalonsocano.gestiondeaverias.repository;

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity.estadoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {

    // ---------------------------------------------
    // Find inventory items by status
    // Buscar artículos del inventario por estado
    // ---------------------------------------------
    List<InventarioEntity> findByEstado(estadoInventario estado);

    // ---------------------------------------------
    // Find inventory items by classroom ID
    // Buscar artículos del inventario por ID de aula
    // ---------------------------------------------
    List<InventarioEntity> findByAulaId(Long aulaId);

    // ---------------------------------------------
    // Find inventory items by name containing text
    // Buscar artículos cuyo nombre contenga un texto
    // ---------------------------------------------
    List<InventarioEntity> findByNombreContainingIgnoreCase(String nombre);

    // ---------------------------------------------
    // Find inventory items by QR code
    // Buscar artículos por código QR
    // ---------------------------------------------
    InventarioEntity findByCodigo_QR(String codigo_QR);
}