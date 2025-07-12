package prueba;

public class DetallePedido {
    private int codigoProducto;
    private String nombreAlbum;
    private int cantidad;
    private double precioUnitario;
    
    // Getters y setters
    public int getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(int codigoProducto) { this.codigoProducto = codigoProducto; }
    public String getNombreAlbum() { return nombreAlbum; }
    public void setNombreAlbum(String nombreAlbum) { this.nombreAlbum = nombreAlbum; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}