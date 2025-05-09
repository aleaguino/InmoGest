package InmoGest.Login_Register;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarFormularioLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "cambio", required = false) String cambio,
            @RequestParam(value = "correcto", required = false) String correcto,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
        }

        if (success != null) {
            model.addAttribute("success", "Contraseña cambiada correctamente. Por favor, inicia sesión.");
        }

        if (cambio != null) {
            model.addAttribute("cambio", "Contraseña actual incorrecta. Por favor, inicie sesión de nuevo.");
        }

        if (correcto != null) {
            model.addAttribute("correcto", "Contraseña cambiada correctamente.");
        }

        return "login";
    }
}
