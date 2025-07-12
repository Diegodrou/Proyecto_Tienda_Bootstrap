package prueba;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ControlDeAcceso extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); // Obtiene la sesión sin crear una nueva
        

        if (session != null && session.getAttribute("codigo") != null) {
            // Usuario ya ha hecho login - redirige a página de usuario

            response.sendRedirect(request.getContextPath() +"/usuario.jsp");
        } else {
            // Usuario no ha hecho login - redirige a página de login/registro

            response.sendRedirect(request.getContextPath() + "/acceso_denegado.html");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
