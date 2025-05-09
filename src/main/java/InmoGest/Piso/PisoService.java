package InmoGest.Piso;

import InmoGest.Usuario.Usuario;
import InmoGest.Usuario.UsuarioService;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PisoService {

    @Autowired
    private PisoRepository pisoRepository;
    @Autowired
    private UsuarioService usuarioService; 
    

    public List<Piso> obtenerPisosPorUsuario(Usuario usuario) {
        return pisoRepository.findByUsuario(usuario);
    }

    
    @Transactional
    public void guardarPiso(Piso piso, Long idUsuario) {
    Usuario usuario = usuarioService.obtenerUsuarioPorId(idUsuario);
    piso.setUsuario(usuario);
    pisoRepository.save(piso);
}
    public Piso obtenerPisoPorId(Long id) {
    return pisoRepository.findById(id).orElse(null);
    }

    @Transactional
    public void eliminarPiso(Long id) {
    pisoRepository.deleteById(id);
    }
    
    public List<Piso> buscarPisosPorDireccionYUsuario(String direccion, Usuario usuario) {
        return pisoRepository.findByUbicacionContainingAndUsuario(direccion, usuario);
    }
        
}

