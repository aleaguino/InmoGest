package InmoGest.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Usuario.
 * Permite operaciones CRUD sobre los usuarios registrados en la aplicación.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
