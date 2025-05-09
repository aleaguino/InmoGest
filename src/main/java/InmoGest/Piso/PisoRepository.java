package InmoGest.Piso;

import InmoGest.Usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PisoRepository extends JpaRepository<Piso, Long> {
    List<Piso> findByUsuario(Usuario usuario);
    List<Piso> findByUbicacionContainingAndUsuario(String ubicacion, Usuario usuario);
}

