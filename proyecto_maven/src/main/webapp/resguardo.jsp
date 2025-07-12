<%@page import="java.util.ArrayList, prueba.*,java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Confirmación de Pedido</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="stylesheets/pago.css">
</head>
<body>
    <div class="container order-container">
        <h1 class="text-center mb-4">Confirmación de Pedido</h1>
        
        <% 
            // Obtener datos de sesión
            int codigo = (Integer) session.getAttribute("codigo");
            ArrayList<Producto> carrito = (ArrayList<Producto>) session.getAttribute("carrito");
            
            // Obtener datos del usuario
            String nombre = "", apellidos = "", direccion = "", telefono = "";
            AccesoBD con = AccesoBD.getInstance();
            try (PreparedStatement ps = con.prepareStatementWrap(
                 "SELECT nombre, apellidos, domicilio, telefono FROM usuarios WHERE codigo = ?")) {
                ps.setInt(1, codigo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        nombre = rs.getString("nombre");
                        apellidos = rs.getString("apellidos");
                        direccion = rs.getString("domicilio");
                        telefono = rs.getString("telefono");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            double total = 0;
            if (carrito != null) {
                for (Producto p : carrito) {
                    total += p.getPrecio() * p.getCantidad();
                }
            }
        %>
        
        <h2 class="section-title">Productos en tu pedido</h2>
        <table class="table product-table">
            <thead>
                <tr>
                    <th>Producto</th>
                    <th>Cantidad</th>
                    <th>Precio Unitario</th>
                    <th>Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <% if (carrito != null) { 
                    for (Producto p : carrito) { 
                        double subtotal = p.getPrecio() * p.getCantidad(); %>
                <tr>
                    <td><%= p.getNombreAlbum() != null ? p.getNombreAlbum() : "" %></td>
                    <td><%= p.getCantidad() %></td>
                    <td><%= String.format("%.2f €", p.getPrecio()) %></td>
                    <td><%= String.format("%.2f €", subtotal) %></td>
                </tr>
                <% } } %>
            </tbody>
            <tfoot class="table-group-divider">
                <tr>
                    <td colspan="3" class="text-end fw-bold">Total:</td>
                    <td class="fw-bold"><%= String.format("%.2f €", total) %></td>
                </tr>
            </tfoot>
        </table>
        
        <form action="tramitacion" method="POST">
            <div class="form-section">
                <h3 class="section-title">Datos Personales</h3>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Nombre</label>
                        <input type="text" name="nombre" value="<%= nombre %>" class="form-control" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Apellidos</label>
                        <input type="text" name="apellidos" value="<%= apellidos %>" class="form-control" required>
                    </div>
                </div>
            </div>
            
            <div class="form-section">
                <h3 class="section-title">Dirección de Envío</h3>
                <div class="mb-3">
                    <label class="form-label">Dirección</label>
                    <input type="text" name="direccion" value="<%= direccion %>" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Teléfono</label>
                    <input type="tel" name="telefono" value="<%= telefono %>" class="form-control" required>
                </div>
            </div>
            
            <div class="form-section">
                <h3 class="section-title">Método de Pago</h3>
                <div class="payment-method form-check">
                    <input class="form-check-input" type="radio" name="metodoPago" id="tarjeta" value="tarjeta" checked>
                    <label class="form-check-label" for="tarjeta">Tarjeta de Crédito</label>
                </div>
                <div class="payment-method form-check">
                    <input class="form-check-input" type="radio" name="metodoPago" id="transferencia" value="transferencia">
                    <label class="form-check-label" for="transferencia">Transferencia Bancaria</label>
                </div>
                <div class="payment-method form-check">
                    <input class="form-check-input" type="radio" name="metodoPago" id="contrareembolso" value="contrareembolso">
                    <label class="form-check-label" for="contrareembolso">Contra Reembolso</label>
                </div>
                
                <div id="datosTarjeta">
                    <div class="mb-3">
                        <label class="form-label">Número de Tarjeta</label>
                        <input type="text" name="numeroTarjeta" class="form-control" placeholder="1234 5678 9012 3456">
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Fecha Caducidad</label>
                            <input type="text" name="caducidadTarjeta" class="form-control" placeholder="MM/AA">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">CVV</label>
                            <input type="text" name="cvvTarjeta" class="form-control" placeholder="123">
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="d-flex justify-content-end gap-3 mt-4">
                <button type="button" onclick="window.location.href='carrito.html'" class="btn btn-cancel px-4 py-2">
                    Cancelar Pedido
                </button>
                <button type="submit" class="btn btn-confirm px-4 py-2">
                    Confirmar Pedido
                </button>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script>
        // Mostrar/ocultar campos de tarjeta
        document.querySelectorAll('input[name="metodoPago"]').forEach(radio => {
            radio.addEventListener('change', function() {
                document.getElementById('datosTarjeta').style.display = 
                    this.value === 'tarjeta' ? 'block' : 'none';
            });
        });
        
        // Inicialmente ocultar campos si no está seleccionado
        document.addEventListener('DOMContentLoaded', function() {
            const metodoPago = document.querySelector('input[name="metodoPago"]:checked').value;
            document.getElementById('datosTarjeta').style.display = 
                metodoPago === 'tarjeta' ? 'block' : 'none';
        });
    </script>
</body>
</html>