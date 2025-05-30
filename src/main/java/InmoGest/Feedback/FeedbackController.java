package InmoGest.Feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * Muestra el formulario de feedback para que el usuario pueda enviar su opinión.
     */
    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }

    /**
     * Procesa el formulario de feedback enviado por el usuario y guarda el mensaje.
     * Muestra un mensaje de éxito y redirige a la lista de pisos.
     */
    @PostMapping("/feedback")
    public String submitFeedback(Feedback feedback, Model model) {
        feedbackService.saveFeedback(feedback);
        model.addAttribute("successMessage", "Mensaje enviado correctamente. Redirigiendo a la lista de pisos...");
        return "redirect:/piso/piso";
    }
}
