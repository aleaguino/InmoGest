package InmoGest.Login_Register;

import InmoGest.Usuario.Usuario;
import InmoGest.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarFormularioRegistro(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuarioService.guardarUsuario(usuario);
            return "redirect:/login?success=register";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("errorRegistro", "El nombre de usuario ya está en uso.");
            return "registro";
        }
    }
}
