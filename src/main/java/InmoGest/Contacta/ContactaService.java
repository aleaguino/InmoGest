package InmoGest.Contacta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactaService {

    @Autowired
    private ContactaRepository contactaRepository;

    public void saveContacta(Contacta contacta) {
        contactaRepository.save(contacta);
    }

    public java.util.List<Contacta> obtenerTodosLosContactas() {
        return contactaRepository.findAll();
    }
}
