package prueba;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CancelarPedidoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener el ID del pedido a cancelar
        int pedidoId = Integer.parseInt(request.getParameter("pedidoId"));
        
        PreparedStatement stmt = null;
        
        try {
            // Establecer conexión con la base de datos
            AccesoBD con = AccesoBD.getInstance();
            
            // 1. Verificar si el pedido puede ser cancelado (estado válido)
            // 2. Actualizar estado del pedido a "cancelado" (asumiendo que estado=0 es cancelado)
            String sql = "UPDATE pedidos SET estado = 5 WHERE codigo = ? AND estado != 0";
            stmt = con.prepareStatementWrap(sql);
            stmt.setInt(1, pedidoId);
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                request.getSession().setAttribute("mensajePedido", "Pedido cancelado exitosamente");
            } else {
                request.getSession().setAttribute("errorPedido", "No se pudo cancelar el pedido. Puede que ya esté cancelado o no exista.");
            }
            
        } catch (Exception e) {
            // Manejar errores y hacer rollback
            try {
                System.out.println(e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            request.getSession().setAttribute("errorPedido", "Error al cancelar el pedido: " + e.getMessage());
        }
        
        // Redirigir de vuelta a la página de pedidos
        response.sendRedirect("usuario.jsp"); // Ajusta esta URL según tu aplicación
    }
}