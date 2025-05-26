package InmoGest.FondosIndexados;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "fondos")
public class Fondo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double cantidad;
    private String tipo;
    private boolean acepta;
    private String pais;

    @ElementCollection
    @CollectionTable(name = "fondo_acciones", joinColumns = @JoinColumn(name = "fondo_id"))
    @Column(name = "accion")
    private List<String> acciones;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isAcepta() { return acepta; }
    public void setAcepta(boolean acepta) { this.acepta = acepta; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public List<String> getAcciones() { return acciones; }
    public void setAcciones(List<String> acciones) { this.acciones = acciones; }
}
