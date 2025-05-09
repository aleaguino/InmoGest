package InmoGest.Contacta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactaRepository extends JpaRepository<Contacta, Long> {
}
