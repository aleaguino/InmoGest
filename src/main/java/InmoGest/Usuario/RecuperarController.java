package InmoGest.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecuperarController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/recuperar")
    public String mostrarFormularioRecuperar(Model model) {
        model.addAttribute("error", false);
        return "recuperar";
    }

    @PostMapping("/cambiarContraseña")
    public String cambiarContraseña(@RequestParam(required = false) String username,
                                    @RequestParam(required = false) String fecha,
                                    @RequestParam(required = false) Long id,
                                    @RequestParam(required = false) String nuevaContraseña,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (username != null && fecha != null) {
            // Procesar formulario de recuperación
            Usuario usuario = usuarioService.findByUsername(username);
            if (usuario != null && usuario.getFecha().toString().equals(fecha)) {
                model.addAttribute("usuario", usuario);
                return "cambiarContraseña";
            } else {
                model.addAttribute("error", "Usuario o fecha de nacimiento incorrectos.");
                return "recuperar";
            }
        } else if (id != null && nuevaContraseña != null) {
            // Procesar cambio de contraseña
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario != null) {
                usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
                usuarioService.save(usuario);
                redirectAttributes.addFlashAttribute("correcto", "Contraseña cambiada correctamente.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "Error al cambiar la contraseña. Por favor, inténtelo de nuevo.");
                return "redirect:/recuperar";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Datos incompletos. Por favor, inténtelo de nuevo.");
            return "redirect:/recuperar";
        }
    }
}
