package InmoGest.Contacta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactaController {

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
}
