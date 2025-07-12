package prueba;

import java.util.Date;
import java.util.List;

public class Pedido {
    private int codigo;
    private Date fecha;
    private double importe;
    private String estado;
    private String nombreAlbum;
    private List<DetallePedido> detalles;
    
    public List<DetallePedido> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getNombreAlbum() { return nombreAlbum; }
    public void setNombreAlbum(String nombreAlbum) { this.nombreAlbum = nombreAlbum; }
}