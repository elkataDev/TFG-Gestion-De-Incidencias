package iesalonsocano.gestiondeaverias.services.impl; // Se recomienda usar un subpaquete 'impl'

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.repository.InventarioRepository;
import iesalonsocano.gestiondeaverias.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de InventarioService.
 * Implementation of InventarioService.
 */
@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    public List<InventarioEntity> findAll() {
        // Devuelve todos los artículos. / Returns all items.
        return inventarioRepository.findAll();
    }

    @Override
    public Optional<InventarioEntity> findById(Long id) {
        // Busca un artículo por ID. / Finds an item by ID.
        return inventarioRepository.findById(id);
    }

    @Override
    public InventarioEntity save(InventarioEntity inventario) {
        // Guarda el artículo. / Saves the item.
        return inventarioRepository.save(inventario);
    }

    @Override
    public void deleteById(Long id) {
        // Elimina el artículo por ID. / Deletes the item by ID.
        inventarioRepository.deleteById(id);
    }

    @Override
    public List<InventarioEntity> findByEstado(InventarioEntity.estadoInventario estado) {
        // Busca por estado. / Finds by status.
        return inventarioRepository.findByEstado(estado);
    }

    @Override
    public List<InventarioEntity> findByAulaId(Long aulaId) {
        // Busca por aula. / Finds by classroom.
        return inventarioRepository.findByAulaId(aulaId);
    }

    @Override
    public InventarioEntity findByCodigo_QR(String codigo_QR) {
        // Busca por código QR. / Finds by QR code.
        return inventarioRepository.findByCodigo_QR(codigo_QR);
    }
}