package InmoGest.Usuario;

import InmoGest.Contacta.Contacta;
import InmoGest.Piso.Piso;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author aguilar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private boolean activo = false;

    @Column(nullable = false)
    private String rol = "USUARIO";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     public Long getId() {
        return id;
    }

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String telefono;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate fecha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Piso> pisos = new ArrayList<>();

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}

