package prueba;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Tramitacion extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("codigo") == null) {
            response.sendRedirect("loginUsuario.jsp");
            return;
        }

        // Obtener datos del usuario
        int codigoUsuario = (Integer) session.getAttribute("codigo");
        ArrayList<Producto> carrito = (ArrayList<Producto>) session.getAttribute("carrito");
        
        // Validar carrito
        if (carrito == null || carrito.isEmpty()) {
            session.setAttribute("mensajeError", "El carrito está vacío");
            response.sendRedirect("carrito.html");
            return;
        }

        Connection conexion = null;
        PreparedStatement psPedido = null;
        ResultSet generatedKeys = null;
        int codigoPedido = 0;

        try {
            AccesoBD con = AccesoBD.getInstance();
            conexion = con.getConexionBD();
            
            // 1. Obtener código de estado "PENDIENTE"
            int estadoPendiente = obtenerCodigoEstado(conexion, "PENDIENTE");
            
            // 2. Insertar el pedido principal
            String sqlPedido = "INSERT INTO pedidos (persona, fecha, importe, estado) VALUES (?, ?, ?, ?)";
            psPedido = conexion.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            
            psPedido.setInt(1, codigoUsuario);
            psPedido.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            psPedido.setBigDecimal(3, calcularImporteTotal(carrito));
            psPedido.setInt(4, estadoPendiente);
            
            // Ejecutar inserción y obtener ID
            if (psPedido.executeUpdate() == 0) {
                throw new SQLException("Error al crear el pedido principal");
            }
            
            generatedKeys = psPedido.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("No se pudo obtener el ID del pedido creado");
            }
            
            codigoPedido = generatedKeys.getInt(1);
            
            // 3. Insertar detalles del pedido
            insertarDetallesPedido(conexion, codigoPedido, carrito);
            
            // 4. Actualizar stock de productos
            actualizarStock(conexion, carrito);
            
            // 5. Preparar datos para la vista de confirmación
            session.setAttribute("codigoPedido", codigoPedido);
            session.removeAttribute("carrito"); // Limpiar carrito
            
            response.sendRedirect("pedidoFinalizado.jsp");
            
        } catch (SQLException e) {
            manejarError(e, session);
            response.sendRedirect("carrito.html");
        } finally {
            cerrarRecursos(generatedKeys, psPedido, conexion);
        }
    }
    
    // Método para calcular el importe total
    private BigDecimal calcularImporteTotal(ArrayList<Producto> carrito) {
        BigDecimal total = BigDecimal.ZERO;
        for (Producto p : carrito) {
            BigDecimal precio = BigDecimal.valueOf(p.getPrecio());
            BigDecimal cantidad = BigDecimal.valueOf(p.getCantidad());
            total = total.add(precio.multiply(cantidad));
        }
        return total;
    }
    
    // Método para obtener código de estado
    private int obtenerCodigoEstado(Connection conexion, String descripcion) throws SQLException {
        String sql = "SELECT codigo FROM estados WHERE descripcion = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, descripcion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Estado '" + descripcion + "' no encontrado");
            }
        }
    }
    
    // Método para insertar detalles del pedido
    private void insertarDetallesPedido(Connection conexion, int codigoPedido, 
                                      ArrayList<Producto> carrito) throws SQLException {
        String sql = "INSERT INTO detalle (codigo_pedido, codigo_producto, unidades, precio_unitario) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (Producto p : carrito) {
                ps.setInt(1, codigoPedido);
                ps.setInt(2, p.getCodigo());
                ps.setInt(3, p.getCantidad());
                ps.setBigDecimal(4, BigDecimal.valueOf(p.getPrecio()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    // Método para actualizar stock
    private void actualizarStock(Connection conexion, ArrayList<Producto> carrito) throws SQLException {
        String sql = "UPDATE productos SET existencias = existencias - ? WHERE codigo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (Producto p : carrito) {
                ps.setInt(1, p.getCantidad());
                ps.setInt(2, p.getCodigo());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    // Manejo de errores mejorado
    private void manejarError(SQLException e, HttpSession session) {
        String mensaje = "Error al procesar el pedido: ";
        
        if (e.getMessage().contains("foreign key constraint fails")) {
            mensaje += "Error de integridad referencial. ";
            if (e.getMessage().contains("estados")) {
                mensaje += "El estado especificado no existe.";
            } else if (e.getMessage().contains("productos")) {
                mensaje += "Uno de los productos no existe.";
            }
        } else {
            mensaje += e.getMessage();
        }
        
        session.setAttribute("mensajeError", mensaje);
        e.printStackTrace();
    }
    
    // Cierre seguro de recursos
    private void cerrarRecursos(ResultSet rs, Statement stmt, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}