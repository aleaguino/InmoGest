package InmoGest.Piso;

import InmoGest.Usuario.Usuario;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "piso")
public class Piso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name= "ciudad")
    private String ciudad;
    @Column (name = "ubicacion")
    private String ubicacion;
    @Column (name = "año")
    private Integer anno;
    @Column (name = "precio")
    private String precio;
    @Column (name = "estado")
    private String estado;
    
    //DATOS INQUILINO
    @Column (name = "inquilinoNombre")
    private String inquilinoNombre;
    @Column (name = "inquilinoDNI")
    private String inquilinoDNI;
    @Column (name = "inquilinoIBAN")
    private String inquilinoIBAN;
    @Column (name = "observacion")
    private String observacion;
    
    //INGRESOS
    @Column (name = "ingresoMensual")
    private double ingresoMensual;
    
    private double ingresosAnuales;
    
    
    //GASTOS
    @Column (name = "comunidad")
    private double comunidad;
    @Column (name = "ibi")
    private double ibi;
    @Column (name = "seguro")
    private double seguro;
    @Column (name = "reforma")
    private double reforma;
    @Column (name = "averias")
    private double averias;
    @Column (name = "agua")
    private double agua;
    @Column (name = "luz")
    private double luz;
    @Column (name = "gas")
    private double gas;
    
    //IPC
    @Column (name = "porcentajeIPC")
    private double porcentajeIPC;
    @Column(name = "fechaActualizacionIPC")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate fechaActualizacionIPC;

    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Getters y setters, constructores, etc.
    
    public String getCiudad() {
    return ciudad;
}
    public String getUbicacion() {
    return ubicacion;
}
    public String getPrecio() {
    return precio;
}
    public String getInquilinoNombre() {
    return inquilinoNombre;
}
    public String getInquilinoDNI() {
    return inquilinoDNI;
}
    public String getInquilinoIBAN() {
    return inquilinoIBAN;
}
    public String getEstado(){
    return estado;
}
    public double getIngresoMensual(){
    return ingresoMensual; 
}
    public double getComunidad(){
    return comunidad;
}
    public double getIbi(){
    return ibi;
}
    public double getSeguro(){
    return seguro;
}
    public double getReforma(){
    return reforma;
}
    public double getAverias(){
    return averias;
}
    public double getAgua(){
    return agua;
}
    public double getLuz(){
    return luz;  
}
    public double getGas(){
    return gas;
}
    public double getPorcentajeIPC(){
    return porcentajeIPC;
}
    public LocalDate getFechaActualizacionIPC(){
    return fechaActualizacionIPC;
}
    public String getObservacion(){
    return observacion;
}
     
    
    public void setIngresosAnuales(double ingresosAnuales) {
        this.ingresosAnuales = ingresosAnuales;
    }
    
    

}
