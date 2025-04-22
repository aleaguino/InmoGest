package InmoGest.Piso;

import InmoGest.Usuario.Usuario;
import InmoGest.Usuario.UsuarioService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/piso")
public class PisoController {

    @Autowired
    private PisoService pisoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/piso")
    public String listarPisosPorUsuario(Model model, Principal principal) {
        String username = principal.getName();
        Usuario usuario = usuarioService.findByUsername(username);
        List<Piso> pisos = pisoService.obtenerPisosPorUsuario(usuario);
        
        // Calcular el total de ingresos y gastos
        double totalIngresos = pisos.stream().mapToDouble(Piso::getIngresoMensual).sum();
        double totalGastos = pisos.stream().mapToDouble(piso -> piso.getComunidad() + piso.getIbi() + piso.getSeguro() + piso.getAgua() + piso.getLuz() + piso.getGas()).sum();
        double diferencia = totalIngresos - totalGastos;
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("pisos", pisos);
        model.addAttribute("totalIngresos", totalIngresos);
        model.addAttribute("totalGastos", totalGastos);
        model.addAttribute("diferencia", diferencia);
        return "listaPisos";
    }
  
    @GetMapping("/formulario")
    public String mostrarFormularioPiso(Model model, Authentication authentication) {
        String nombreUsuario = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(nombreUsuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("piso", new Piso());
        return "formularioPiso";
    }

   
    @PostMapping("/guardar")
    public String guardarPiso(@ModelAttribute("piso") Piso piso, @RequestParam("idUsuario") Long idUsuario) {
         pisoService.guardarPiso(piso, idUsuario);
        return "redirect:/piso/piso";  // Redirige a dlista de pisos
    }
    
       @GetMapping("/modificar/{id}")
    public String modificarPisoForm(@PathVariable Long id, Model model) {
    Piso piso = pisoService.obtenerPisoPorId(id);
    if (piso != null) {
        model.addAttribute("piso", piso);
        return "formularioEditarPiso";
    } else {
        return "redirect:/piso/piso";
    }
}


    @PostMapping("/modificar/{id}")
    public String modificarPisoSubmit(@PathVariable Long id, @ModelAttribute("piso") Piso piso) {
    Piso pisoExistente = pisoService.obtenerPisoPorId(id);
    if (pisoExistente != null) {
        pisoExistente.setCiudad(piso.getCiudad());
        pisoExistente.setUbicacion(piso.getUbicacion());
        pisoExistente.setAnno(piso.getAnno());
        pisoExistente.setPrecio(piso.getPrecio());
        pisoExistente.setEstado(piso.getEstado());
        
        //INGRESOS
        pisoExistente.setIngresoMensual(piso.getIngresoMensual());
        
        //INQUILINO
        pisoExistente.setInquilinoNombre(piso.getInquilinoNombre());
        pisoExistente.setInquilinoDNI(piso.getInquilinoDNI());
        pisoExistente.setInquilinoIBAN(piso.getInquilinoIBAN());

        //GASTOS
        pisoExistente.setComunidad(piso.getComunidad());
        pisoExistente.setIbi(piso.getIbi());
        pisoExistente.setSeguro(piso.getSeguro());
        pisoExistente.setReforma(piso.getReforma());
        pisoExistente.setAverias(piso.getAverias());
        pisoExistente.setAgua(piso.getAgua());
        pisoExistente.setLuz(piso.getLuz());
        pisoExistente.setGas(piso.getGas());
        
        // IPC
        pisoExistente.setPorcentajeIPC(piso.getPorcentajeIPC());
        pisoExistente.setFechaActualizacionIPC(piso.getFechaActualizacionIPC());
        
        
        pisoService.guardarPiso(pisoExistente, pisoExistente.getUsuario().getId());
    }
    return "redirect:/piso/detalles/{id}";
}



    @GetMapping("/eliminar/{id}")
    public String eliminarPiso(@PathVariable Long id) {
        pisoService.eliminarPiso(id);
        return "redirect:/piso/piso";
    }
    
    @GetMapping("/detalles/{id}")
    public String detallesPiso(@PathVariable Long id, Model model) {
    Piso piso = pisoService.obtenerPisoPorId(id);
    
    //CALCULAR INGRESOS ANUALES
    double ingresosAnuales = piso.getIngresoMensual() * 12;
    piso.setIngresosAnuales(ingresosAnuales);
    
    model.addAttribute("piso", piso);
    return "detallesPiso";
}
}
