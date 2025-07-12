package prueba;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Registro extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Obtener parámetros del formulario
        String nombre_de_usuario = request.getParameter("usuario");
        String nombre = request.getParameter("nombre");
        String apelidos = request.getParameter("apellidos");
        String password = request.getParameter("clave");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");

        // Validaciones
        StringBuilder errores = new StringBuilder();
        boolean isValid = validar_datos(nombre_de_usuario,nombre,apelidos,password,direccion,telefono,errores);
        HttpSession session = request.getSession(true);
        if (!isValid) {
            session.setAttribute("mensaje", errores.toString().replace("\\n", "<br>"));
            return;
        }

        AccesoBD con = AccesoBD.getInstance();
        boolean exito = con.agregarUsuario(nombre_de_usuario, nombre, apelidos, password, direccion, telefono,errores);
        if (exito){
            response.sendRedirect("loginUsuario.jsp");
        }
        else{
            session.setAttribute("mensaje", errores.toString().replace("\\n", "<br>"));
            response.sendRedirect("registroUsuario.jsp");
        }

    }

    private boolean validar_datos(String nombre_de_usuario,String nombre, String apellidos, String password, String direccion ,String telefono,StringBuilder errores){
        boolean isValid = true;
        
        if (password.length() < 8) {
            isValid = false;
            errores.append("La contraseña debe tener al menos 8 caracteres.\\n");
        }

        if (password.length() > 40) {
            isValid = false;
            errores.append("La contraseña debe tener menos 40 caracteres.\\n");
        }

        if (nombre_de_usuario.length() > 32) {
            isValid = false;
            errores.append("El nombre de usuario debe tener menos 32 caracteres.\\n");
        }
        if (nombre_de_usuario.length() < 3) {
            isValid = false;
            errores.append("El nombre de usuario debe tener al menos 3 caracteres.\\n");
        }

        return isValid;
    }
}
