import InmoGest.Usuario.Usuario;
import InmoGest.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/editar")
    public String mostrarFormularioEdicion(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(currentUsername);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "editarUsuario"; // Nombre de la plantilla HTML para editar usuario
    }

    @PostMapping("/editar")
    public String editarUsuario(@RequestParam String password,
                                @RequestParam(required = false) String newPassword,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(currentUsername);

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "editarUsuario";
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(newPassword));
        }

        usuarioService.save(usuario);

        model.addAttribute("success", "Usuario actualizado correctamente");
        return "editarUsuario";
    }
}
