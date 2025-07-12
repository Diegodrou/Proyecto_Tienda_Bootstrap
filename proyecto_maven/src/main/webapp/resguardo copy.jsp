    <%@page import="java.util.ArrayList, prueba.*,java.sql.*"%>
    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmación de Pedido</title>
    </head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body { font-family: Arial, sans-serif; text-align: center; }
        h2 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid black; padding: 10px; text-align: left; }
        th { background-color: #f4f4f4; }
        .total { font-size: 18px; font-weight: bold; margin-top: 20px; }
        .fecha { font-size: 14px; color: gray; margin-bottom: 10px; }
        button { padding: 10px; margin-top: 20px; font-size: 16px; cursor: pointer; }
    </style>
    <body>
        <h1>Confirmación de Pedido</h1>
        
        <% 
            // Obtener el ID de usuario de la sesión
            int codigo = (Integer) session.getAttribute("codigo");
            ArrayList<Producto> carrito = (ArrayList<Producto>) session.getAttribute("carrito");
            
            // Obtener datos del usuario desde la BD
            AccesoBD con = AccesoBD.getInstance();
            ResultSet rsUsuario = null;
            PreparedStatement psUsuario = null;
            
            String nombre = "";
            String apellidos = "";
            String direccion = "";
            String telefono = "";
            
            try {
                String sqlUsuario = "SELECT nombre, apellidos, domicilio,telefono " +
                                "FROM usuarios WHERE codigo = ?";
                psUsuario = con.prepareStatementWrap(sqlUsuario);
                psUsuario.setInt(1, codigo);
                rsUsuario = psUsuario.executeQuery();
                
                if (rsUsuario.next()) {
                    nombre = rsUsuario.getString("nombre");
                    apellidos = rsUsuario.getString("apellidos");
                    direccion = rsUsuario.getString("domicilio");
                    telefono = rsUsuario.getString("telefono");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rsUsuario != null) rsUsuario.close();
                    if (psUsuario != null) psUsuario.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        %>
        
        <h2>Productos en tu pedido:</h2>
        <table border="1">
            <tr>
                <th>Producto</th>
                <th>Cantidad</th>
                <th>Precio Unitario</th>
                <th>Subtotal</th>
            </tr>
            <% 
                double total = 0;
                if (carrito != null) {
                    for (Producto p : carrito) {
                        double subtotal = p.getPrecio() * p.getCantidad();
                        total += subtotal;
            %>
            <tr>
                <td><%= p.getNombreAlbum() != null ? p.getNombreAlbum() : "" %></td>
                <td><%= p.getCantidad() %></td>
                <td><%= String.format("%.2f €", p.getPrecio()) %></td>
                <td><%= String.format("%.2f €", subtotal) %></td>
            </tr>
            <% 
                    }
                }
            %>
            <tr>
                <td colspan="4" align="right"><strong>Total:</strong></td>
                <td><strong><%= String.format("%.2f €", total) %></strong></td>
            </tr>
        </table>
        
        <h2>Datos de Envío y Pago</h2>
        <form action="Tramitacion" method="POST">
            <h3>Datos Personales</h3>
            <label>Nombre:</label>
            <input type="text" name="nombre" value="<%= nombre %>" class="form-control" required><br>
            
            <label>Apellidos:</label>
            <input type="text" name="apellidos" value="<%= apellidos %>" class="form-control"  required><br>
            
            <h3>Dirección de Envío</h3>
            <label>Dirección:</label>
            <input type="text" name="direccion" value="<%= direccion %>" class="form-control" required><br>
            
            <label>Teléfono:</label>
            <input type="tel" name="telefono" value="<%= telefono %>" class="form-control" required><br>

            
            <h3>Método de Pago</h3>
            <input type="radio" name="metodoPago" value="tarjeta" class="form-control" checked> Tarjeta de Crédito<br>
            <input type="radio" name="metodoPago" value="transferencia" class="form-control"> Transferencia Bancaria<br>
            <input type="radio" name="metodoPago" value="contrareembolso" class="form-control"> Contra Reembolso<br>
            
            <div id="datosTarjeta">
                <label>Número de Tarjeta:</label>
                <input type="text" name="numeroTarjeta" class="form-control"><br>
                
                <label>Fecha Caducidad:</label>
                <input type="text" name="caducidadTarjeta" placeholder="MM/AA" class="form-control"><br>
                
                <label>CVV:</label>
                <input type="text" name="cvvTarjeta" class="form-control"><br>
            </div>
            
            <input type="submit" value="Confirmar Pedido" class="form-control">
            <input type="button" value="Cancelar Pedido" onclick="window.location.href='carrito.html'" class="form-control">
        </form>
        
        <script>
            // Mostrar/ocultar campos de tarjeta según selección
            document.querySelectorAll('input[name="metodoPago"]').forEach(radio => {
                radio.addEventListener('change', function() {
                    document.getElementById('datosTarjeta').style.display = 
                        this.value === 'tarjeta' ? 'block' : 'none';
                });
            });
            
            // Inicialmente ocultar campos de tarjeta si no está seleccionado
            document.addEventListener('DOMContentLoaded', function() {
                const metodoPago = document.querySelector('input[name="metodoPago"]:checked').value;
                document.getElementById('datosTarjeta').style.display = 
                    metodoPago === 'tarjeta' ? 'block' : 'none';
            });
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    </body>
    </html>