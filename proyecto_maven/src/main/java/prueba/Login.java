package prueba;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Login extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Nombre del usuario
        String usuario = request.getParameter("usuario");
    
        // Clave
        String clave = request.getParameter("clave");
    
        // URL a la que debemos volver
    
        String url = request.getParameter("url");
    
        // Accedemos al entorno de sesión y si no está creado lo creamos
    
        HttpSession session = request.getSession(true);
    
        AccesoBD con = AccesoBD.getInstance();
    
        if ((usuario != null) && (clave != null)) {
            int codigo = con.comprobarUsuarioBD(usuario,clave);
            if (codigo>0) {
                session.setAttribute("codigo",codigo);
                List<String> atributos = con.getAtributosSession(codigo);
                session.setAttribute("nombre", atributos.get(0));
                session.setAttribute("apellidos", atributos.get(1));
                session.setAttribute("direccion", atributos.get(2));
                session.setAttribute("telefono", atributos.get(3));
            }
            else {
                session.setAttribute("mensaje","Usuario y/o clave incorrectos");
            }
        }
    
        request.getRequestDispatcher(url).forward(request, response);
    
    }
}
