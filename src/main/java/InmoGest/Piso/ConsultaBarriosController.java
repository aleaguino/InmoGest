package InmoGest.Piso;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsultaBarriosController {
    @GetMapping("/consultaBarrios.html")
    public String mostrarConsultaBarrios() {
        return "consultaBarrios";
    }
}
