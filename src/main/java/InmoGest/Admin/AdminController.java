package InmoGest.Admin;

import InmoGest.Usuario.Usuario;
import InmoGest.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private InmoGest.Contacta.ContactaService contactaService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        List<Integer> edades = new ArrayList<>();
        int suma = 0;
        LocalDate hoy = LocalDate.now();
        for (Usuario u : usuarios) {
            if (u.getFecha() != null) {
                int edad = Period.between(u.getFecha(), hoy).getYears();
                edades.add(edad);
                suma += edad;
            }
        }
        double media = edades.isEmpty() ? 0 : (double) suma / edades.size();
        model.addAttribute("edades", edades);
        model.addAttribute("mediaEdad", media);
        return "admin/dashboard";
    }

    @GetMapping("/admin/mensajes")
    public String verMensajesContacta(Model model) {
        java.util.List<InmoGest.Contacta.Contacta> mensajes = contactaService.obtenerTodosLosContactas();
        model.addAttribute("mensajes", mensajes);
        return "admin/admin_mensajes";
    }
}

