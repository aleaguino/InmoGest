package InmoGest.Contacta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import InmoGest.Usuario.Usuario;
import InmoGest.Usuario.UsuarioService;

@Controller
public class ContactaController {

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private ContactaService contactaService;

    @GetMapping("/contacta")
    public String showContactaForm(Model model) {
        model.addAttribute("contacta", new Contacta());
        return "contacta";
    }

    @PostMapping("/contacta")
    public String submitContacta(Contacta contacta, Model model) {
        contactaService.saveContacta(contacta);
        model.addAttribute("successMessage", "Mensaje enviado correctamente. Redirigiendo a la lista de pisos...");
        return "redirect:/piso/piso";
    }

    @PostMapping("/contacta/conviertete-admin")
    public String convierteteAdmin(@RequestParam String fraseAdmin, Model model, HttpServletRequest request) {
        final String FRASE_SECRETA = "kPaks781Pa!"; // Frase secreta definida por el usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(currentUsername);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (FRASE_SECRETA.equals(fraseAdmin)) {
            usuario.setRol("ADMIN");
            usuarioService.save(usuario);
            // Cerrar sesión
            try {
                request.logout();
            } catch (Exception e) {
                // Ignorar
            }
            return "redirect:/login?admin=true";
        } else {
            model.addAttribute("contacta", new Contacta());
            model.addAttribute("errorAdmin", "Frase incorrecta. Inténtalo de nuevo.");
            return "contacta";
        }
    }
}
