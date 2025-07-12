package prueba;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import jakarta.json.*;
import java.io.InputStreamReader;

public class ProcesarPedido extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);

        // Verificar si el usuario está logueado
        if (sesion == null || sesion.getAttribute("codigo") == null) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(request.getContextPath() + "/loginUsuario.jsp");
            return;
        }
        
        // Limpiar atributos de sesión previos si existen
        if (sesion.getAttribute("carritoJSON") != null) {
            sesion.removeAttribute("carritoJSON");
        }
        if (sesion.getAttribute("mensajeError") != null) {
            sesion.removeAttribute("mensajeError");
        }

        ArrayList<Producto> carritoFinal = new ArrayList<Producto>();
        AccesoBD con = AccesoBD.getInstance();

        try {
            // Leer y parsear el JSON del carrito
            JsonReader jsonReader = Json.createReader(
                    new InputStreamReader(
                            request.getInputStream(), "utf-8"));

            JsonArray carritoJsonArray = jsonReader.readArray();
            jsonReader.close();

            // Procesar cada producto del carrito
            for (int i = 0; i < carritoJsonArray.size(); i++) {
                JsonObject productoJson = carritoJsonArray.getJsonObject(i);
                Producto producto = new Producto();

                // Asignar propiedades básicas del producto
                producto.setCodigo(productoJson.getInt("codigo"));
                producto.setDescripcion(productoJson.getString("descripcion"));
                producto.setImagen(productoJson.getString("imagen"));
                producto.setPrecio((float) productoJson.getJsonNumber("precio").doubleValue());
                producto.setNombreAlbum(productoJson.getString("nombreAlbum"));

                int cantidadDeseada = productoJson.getInt("cantidad");
                int existenciasActuales = con.obtenerExistencias(producto.getCodigo());

                // Ajustar cantidad según disponibilidad
                if (existenciasActuales <= 0) {
                    continue; // Saltar productos sin existencias
                }

                if (cantidadDeseada > existenciasActuales) {
                    producto.setCantidad(existenciasActuales);
                    // Podrías añadir un mensaje informando que se ajustó la cantidad
                } else {
                    producto.setCantidad(cantidadDeseada);
                }

                carritoFinal.add(producto);
            }

            // Guardar en sesión solo si hay productos disponibles
            if (!carritoFinal.isEmpty()) {
                sesion.setAttribute("carrito", carritoFinal);
            } else {
                sesion.setAttribute("mensajeError", 
                    "No hay existencias disponibles para los productos seleccionados");
            }

        } catch (JsonException e) {
            sesion.setAttribute("mensajeError", "Error al procesar el carrito: formato JSON inválido");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON inválido");
            return;
        } catch (Exception e) {
            sesion.setAttribute("mensajeError", "Error al procesar el pedido");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno");
            return;
        }

        //Redirigir a la página de resguardo
        // RequestDispatcher rd = request.getRequestDispatcher("resguardo.jsp");
        // rd.forward(request, response);

        
        // response.sendRedirect(request.getContextPath() + "/resguardo.jsp");
        // return;

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(request.getContextPath() + "/resguardo.jsp");

    }
}