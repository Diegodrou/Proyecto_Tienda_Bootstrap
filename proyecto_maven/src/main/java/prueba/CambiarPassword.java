package prueba;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CambiarPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener parámetros del formulario
        String claveActual = request.getParameter("clave-actual");
        String nuevaClave = request.getParameter("nueva-clave");
        
        // Obtener sesión actual
        HttpSession session = request.getSession();
        
        try {
            int codigoUsuario = (int) session.getAttribute("codigo");
            
            // 1. Verificar que la contraseña actual coincida con la del usuario
            // (Aquí deberías obtener la contraseña real del usuario desde tu base de datos)
            String claveRealUsuario = getCurrentPass(codigoUsuario); // Esto es un ejemplo
            
            if (!claveActual.equals(claveRealUsuario)) {
                session.setAttribute("mensaje", "La contraseña actual no es correcta");
                response.sendRedirect("usuario.jsp");
                return;
            }
            
            // 2. Validar la nueva contraseña (longitud, complejidad, etc.)
            if (nuevaClave == null || nuevaClave.length() < 8) {
                session.setAttribute("mensaje", "La nueva contraseña debe tener al menos 8 caracteres");
                response.sendRedirect("usuario.jsp");
                return;
            }
            
            // 3. Actualizar la contraseña en la base de datos
            actualizarPassword(nuevaClave, codigoUsuario);
            
            // 4. Mostrar mensaje de éxito
            session.setAttribute("mensaje", "Contraseña cambiada exitosamente");
            session.setAttribute("tipoMensaje", "success");
            response.sendRedirect("usuario.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("mensaje", "Error al cambiar la contraseña: " + e.getMessage());
            session.setAttribute("tipoMensaje", "danger");
            response.sendRedirect("usuario.jsp");
        }
    }

    private String getCurrentPass(int codigoUsuario) throws SQLException{
        String currentPass ="";

        AccesoBD con = AccesoBD.getInstance();

    
        String sql = "SELECT clave FROM usuarios WHERE codigo = ?";
        
        try (PreparedStatement ps = con.prepareStatementWrap(sql)) {
            ps.setInt(1, codigoUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentPass = rs.getString("clave");
                }
            }
        }
        


        return currentPass;
    }

    private void actualizarPassword(String nuevaPassword, int codigoUsuario) throws SQLException
    {
        // Validaciones básicas
    if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
        throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");
    }
    
    if (nuevaPassword.length() < 8) {
        throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
    }
    
    AccesoBD con = AccesoBD.getInstance();
    
    try {
        
        
        
        // 2. Actualizar contraseña
        String sql = "UPDATE usuarios SET clave = ? WHERE codigo = ?";
        
        try (PreparedStatement ps = con.prepareStatementWrap(sql)) {
            ps.setString(1, nuevaPassword);
            ps.setInt(2, codigoUsuario);
            
            int resultado = ps.executeUpdate();
            
            if (resultado == 0) {
                throw new SQLException("Usuario no encontrado");
            }
        }
        
        
        

        
    } catch (Exception e) {
        System.out.println(e.getMessage());
    } 
    }
}