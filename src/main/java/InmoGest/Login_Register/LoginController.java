package InmoGest.Login_Register;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.authentication.DisabledException;

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

        if ("register".equals(success)) {
            model.addAttribute("success", "Usuario registrado correctamente. Tu cuenta aun esta pendiente de habilitar por un administrador.");
        } else if ("password".equals(success)) {
            model.addAttribute("success", "Contraseña cambiada correctamente. Por favor, inicia sesión con tu nueva contraseña.");
        } else {
            if (correcto != null) {
                model.addAttribute("correcto", "Contraseña cambiada correctamente.");
            }
        }

        if (cambio != null) {
            model.addAttribute("cambio", "Contraseña actual incorrecta. Por favor, inicie sesión de nuevo.");
        }

        return "login";
    }
}
