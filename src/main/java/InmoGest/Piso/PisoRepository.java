package InmoGest.Piso;

import InmoGest.Usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Piso.
 * Permite operaciones CRUD sobre los pisos registrados por los usuarios.
 */
public interface PisoRepository extends JpaRepository<Piso, Long> {
    List<Piso> findByUsuario(Usuario usuario);
    List<Piso> findByUbicacionContainingAndUsuario(String ubicacion, Usuario usuario);
}
