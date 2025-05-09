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

    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }

    @PostMapping("/feedback")
    public String submitFeedback(Feedback feedback, Model model) {
        feedbackService.saveFeedback(feedback);
        model.addAttribute("successMessage", "Mensaje enviado correctamente. Redirigiendo a la lista de pisos...");
        return "redirect:/piso/piso";
    }
}
