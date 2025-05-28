package InmoGest.Login_Register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import InmoGest.Usuario.UsuarioRepository;

@RestController
public class UsuarioRestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/existe")
    public boolean existeUsuario(@RequestParam String username) {
        return usuarioRepository.existsByUsernameIgnoreCase(username);
    }
}
