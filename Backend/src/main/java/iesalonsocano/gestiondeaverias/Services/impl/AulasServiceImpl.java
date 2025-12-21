package iesalonsocano.gestiondeaverias.Services.impl; // Se recomienda usar un subpaquete 'impl'

import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import iesalonsocano.gestiondeaverias.Repository.AulasRepository;
import iesalonsocano.gestiondeaverias.Services.AulasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de AulasService que se comunica con el repositorio.
 * Implementation of AulasService that communicates with the repository.
 */
@Service // Marca esta clase como un componente de servicio de Spring. / Marks this class as a Spring service component.
public class AulasServiceImpl implements AulasService {

    @Autowired // Inyecta el repositorio. / Injects the repository.
    private AulasRepository aulasRepository;

    @Override
    public List<AulasEntity> findAll() {
        // Devuelve la lista de todas las aulas. / Returns the list of all classrooms.
        return aulasRepository.findAll();
    }

    @Override
    public Optional<AulasEntity> findById(Long id) {
        // Busca un aula por ID. / Finds a classroom by ID.
        return aulasRepository.findById(id);
    }

    @Override
    public AulasEntity save(AulasEntity aula) {
        // Guarda la entidad. / Saves the entity.
        return aulasRepository.save(aula);
    }

    @Override
    public void deleteById(Long id) {
        // Elimina el aula. / Deletes the classroom.
        aulasRepository.deleteById(id);
    }

    @Override
    public Optional<AulasEntity> findByNombre(String nombre) {
        // Busca un aula por nombre. / Finds a classroom by name.
        return aulasRepository.findByNombre(nombre);
    }
}