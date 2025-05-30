package InmoGest.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UsuarioController {

    // Listar todos los usuarios (solo admin)
    @GetMapping("/admin/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> todos = usuarioService.obtenerTodosLosUsuarios();
        List<Usuario> admins = new ArrayList<>();
        List<Usuario> usuarios = new ArrayList<>();
        for (Usuario u : todos) {
            if ("ADMIN".equals(u.getRol())) {
                admins.add(u);
            } else {
                usuarios.add(u);
            }
        }
        model.addAttribute("admins", admins);
        model.addAttribute("usuarios", usuarios);
        return "admin/listaUsuarios";
    }

    // Eliminar usuario (solo admin)
    @PostMapping("/admin/usuarios/eliminar")
    public String eliminarUsuario(@RequestParam Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    // Inhabilitar usuario (solo admin)
    @PostMapping("/admin/usuarios/inhabilitar")
    public String inhabilitarUsuario(@RequestParam Long id) {
        usuarioService.inhabilitarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    // Habilitar usuario (solo admin)
    @PostMapping("/admin/usuarios/habilitar")
    public String habilitarUsuario(@RequestParam Long id) {
        usuarioService.habilitarUsuario(id);
        return "redirect:/admin/usuarios";
    }


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
                                @RequestParam(required = false) String confirmPassword,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(currentUsername);

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return "redirect:/login?cambio=true";
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Las nuevas contraseñas no coinciden");
                return "editarUsuario";
            }
            usuario.setPassword(passwordEncoder.encode(newPassword));
            usuarioService.save(usuario);
            return "redirect:/login?success=password"; // Redirigir al login con parámetro de éxito
        }

        model.addAttribute("error", "La nueva contraseña no puede estar vacía");
        return "editarUsuario";
    }
}
