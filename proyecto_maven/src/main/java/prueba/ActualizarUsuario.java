package prueba;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class ActualizarUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get current session and user code
        HttpSession session = request.getSession();
        Integer codigoUsuario = (Integer) session.getAttribute("codigo");
        
        // Check if user is logged in
        if (codigoUsuario == null) {
            response.sendRedirect("loginUsuario.jsp");
            return;
        }
        
        // Get form parameters
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        
        // Validate parameters
        if (nombre == null || apellidos == null || direccion == null || telefono == null) {
            session.setAttribute("error", "Todos los campos son obligatorios");
            response.sendRedirect("usuario.jsp");
            return;
        }
        
        // Update user in database
        try {
            boolean actualizado = actualizarUsuario(codigoUsuario, nombre, apellidos, direccion, telefono);
            
            if (actualizado) {
                // Update session attributes
                session.setAttribute("nombre", nombre);
                session.setAttribute("apellidos", apellidos);
                session.setAttribute("direccion", direccion);
                session.setAttribute("telefono", telefono);
                
                session.setAttribute("exito", "Información actualizada correctamente");
            } else {
                session.setAttribute("error", "No se pudo actualizar la información");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Error al actualizar en la base de datos: " + e.getMessage());
        }
        
        response.sendRedirect("usuario.jsp");
    }
    
    private boolean actualizarUsuario(int codigo, String nombre, String apellidos, 
                                    String direccion, String telefono) throws SQLException {
        
        String sql = "UPDATE usuarios SET nombre = ?, apellidos = ?, domicilio = ?, telefono = ? " +
                     "WHERE codigo = ?";
        
        AccesoBD db = AccesoBD.getInstance();
        
        try (PreparedStatement ps = db.prepareStatementWrap(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, direccion);
            ps.setString(4, telefono);
            ps.setInt(5, codigo);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }
}