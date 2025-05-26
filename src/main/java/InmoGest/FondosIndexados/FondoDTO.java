package InmoGest.FondosIndexados;

public class FondoDTO {
    private String nombre;
    private double cantidad;
    private String tipo;
    private boolean acepta;
    private String pais;
    private java.util.List<String> acciones;

    // Getters y setters
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

    public java.util.List<String> getAcciones() { return acciones; }
    public void setAcciones(java.util.List<String> acciones) { this.acciones = acciones; }
}
