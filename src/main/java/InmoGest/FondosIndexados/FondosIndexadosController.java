package InmoGest.FondosIndexados;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

@Controller
public class FondosIndexadosController {

    private final FondoRepository fondoRepository;

    public FondosIndexadosController(FondoRepository fondoRepository) {
        this.fondoRepository = fondoRepository;
    }
    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping("/fondos-indexados")
    public String fondosIndexados() {
        return "fondosIndexados";
    }

    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping("/creador-fondos")
    public String creadorFondos(org.springframework.ui.Model model) {
        java.util.List<Fondo> fondos = fondoRepository.findAll();
        java.util.List<FondoDTO> fondosDTO = new java.util.ArrayList<>();
        for (Fondo fondo : fondos) {
            FondoDTO dto = new FondoDTO();
            dto.setNombre(fondo.getNombre());
            dto.setCantidad(fondo.getCantidad());
            dto.setTipo(fondo.getTipo());
            dto.setAcepta(fondo.isAcepta());
            dto.setPais(fondo.getPais());
            dto.setAcciones(fondo.getAcciones() != null ? fondo.getAcciones() : new java.util.ArrayList<>());
            fondosDTO.add(dto);
        }
        model.addAttribute("fondos", fondosDTO);
        return "creadorFondos";
    }

    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping("/guia-inversion")
    public String guiaInversion() {
        return "guiaInversion";
    }

    // Endpoint API para guardar fondos
    @PreAuthorize("hasRole('USUARIO')")
    @PostMapping(value = "/api/fondos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registrarFondo(@RequestBody FondoDTO fondoDTO) {
        try {
            Fondo fondo = new Fondo();
            fondo.setNombre(fondoDTO.getNombre());
            fondo.setCantidad(fondoDTO.getCantidad());
            fondo.setTipo(fondoDTO.getTipo());
            fondo.setAcepta(fondoDTO.isAcepta());
            fondo.setPais(fondoDTO.getPais());
            if (fondoDTO.getAcciones() != null) {
                fondo.setAcciones(fondoDTO.getAcciones());
            }
            fondoRepository.save(fondo);
            return ResponseEntity.ok().body("{\"mensaje\":\"Fondo registrado correctamente\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"mensaje\":\"Error al guardar el fondo\"}");
        }
    }
}

