<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="prueba.Pedido"%>
<%@page import="prueba.AccesoBD"%>
<%@page import="java.sql.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Perfil de Usuario - VinLo</title>
    <link rel="icon" type="image/ico" href="img/logo.png" sizes="64x64">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="stylesheets/usuario.css">
   <style>
        .badge-pendiente { background-color: #ffc107; color: #000; }
        .badge-enviado { background-color: #17a2b8; color: #fff; }
        .badge-entregado { background-color: #28a745; color: #fff; }
        .badge-cancelado { background-color: #dc3545; color: #fff; }

    </style>
</head>
<body>
    <div class="container py-4">
        <%
            // Verificar sesión
            HttpSession sesion = request.getSession(false);
            if (sesion == null || sesion.getAttribute("codigo") == null) {
                response.sendRedirect("loginUsuario.jsp");
                return;
            }

            // Obtener datos del usuario
            int codigoUsuario = (Integer) sesion.getAttribute("codigo");
            String nombre = (String) sesion.getAttribute("nombre");
            String apellidos = (String) sesion.getAttribute("apellidos");
            String direccion = (String) sesion.getAttribute("direccion");
            String telefono = (String) sesion.getAttribute("telefono");
            
            // Obtener pedidos del usuario desde la BD
            List<Pedido> pedidos = new ArrayList<>();
            Connection conexion = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            try {
                AccesoBD con = AccesoBD.getInstance();
                
                // Consulta para obtener los pedidos del usuario
                String sql = "SELECT p.codigo, p.fecha, p.importe, e.descripcion as estado, " +
                             "pr.nombreAlbum " +
                             "FROM pedidos p " +
                             "JOIN estados e ON p.estado = e.codigo " +
                             "JOIN detalle d ON p.codigo = d.codigo_pedido " +
                             "JOIN productos pr ON d.codigo_producto = pr.codigo " +
                             "WHERE p.persona = ? " +
                             "ORDER BY p.fecha DESC";
                
                ps = con.prepareStatementWrap(sql);
                ps.setInt(1, codigoUsuario);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setCodigo(rs.getInt("codigo"));
                    pedido.setFecha(rs.getDate("fecha"));
                    pedido.setImporte(rs.getDouble("importe"));
                    pedido.setEstado(rs.getString("estado"));
                    pedido.setNombreAlbum(rs.getString("nombreAlbum"));
                    pedidos.add(pedido);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Cerrar recursos
                try { if (rs != null) rs.close(); } catch (SQLException e) {}
                try { if (ps != null) ps.close(); } catch (SQLException e) {}
            }
        %>
        
        <h2 class="text-center mt-3 mb-4">Tu Perfil</h2>
        
        <!-- Formulario de datos personales -->
        <form action="actualizarUsuario" method="POST" class="mb-4">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Nombre</label>
                    <input type="text" class="form-control" name="nombre" 
                           value="<%= nombre != null ? nombre : "" %>" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Apellidos</label>
                    <input type="text" class="form-control" name="apellidos" 
                           value="<%= apellidos != null ? apellidos : "" %>" required>
                </div>
            </div>
            <div class="row">
                <div class="col-md-8 mb-3">
                    <label class="form-label">Dirección</label>
                    <input type="text" class="form-control" name="direccion" 
                           value="<%= direccion != null ? direccion : "" %>" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Teléfono</label>
                    <input type="tel" class="form-control" name="telefono" 
                           value="<%= telefono != null ? telefono : "" %>" required>
                </div>
            </div>
            <button type="submit" class="btn btn-primary w-100">Actualizar Información</button>
        </form>
        
        <!-- Formulario para cambiar contraseña -->
        <h4 class="text-center mt-4 mb-3">Cambiar Contraseña</h4>
        <form action="CambiarPassword" method="POST" class="mb-5">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Contraseña Actual</label>
                    <input type="password" class="form-control" name="clave-actual" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Nueva Contraseña</label>
                    <input type="password" class="form-control" name="nueva-clave" required>
                </div>
            </div>
            <button type="submit" class="btn btn-warning w-100">Cambiar Contraseña</button>
        </form>
        
        <!-- Sección de pedidos -->
        <div class="pedido-container p-4">
            <h3 class="text-center mb-4">Tus Pedidos</h3>
            
            <% if (pedidos.isEmpty()) { %>
                <div class="alert alert-info text-center">
                    No tienes pedidos realizados.
                </div>
            <% } else { %>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>ID Pedido</th>
                                <th>Fecha</th>
                                <th>Álbum</th>
                                <th>Total</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Pedido pedido : pedidos) { %>
                            <tr>
                                <td><%= pedido.getCodigo() %></td>
                                <td><%= new SimpleDateFormat("dd/MM/yyyy").format(pedido.getFecha()) %></td>
                                <td><%= pedido.getNombreAlbum() %></td>
                                <td><%= String.format("%.2f €", pedido.getImporte()) %></td>
                                <td>
                                    <span class="badge <%= 
                                        pedido.getEstado().equalsIgnoreCase("PENDIENTE") ? "badge-pendiente" :
                                        pedido.getEstado().equalsIgnoreCase("ENVIADO") ? "badge-enviado" :
                                        pedido.getEstado().equalsIgnoreCase("ENTREGADO") ? "badge-entregado" :
                                        "badge-cancelado" %>">
                                        <%= pedido.getEstado() %>
                                    </span>
                                </td>
                                <td>
                                    <a href="detallePedido.jsp?id=<%= pedido.getCodigo() %>" 
                                       class="btn btn-sm btn-info">Ver Detalles</a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>
        
        <!-- Botones de navegación -->
        <div class="d-flex justify-content-between mt-4">
            <a href="index.jsp" class="btn btn-secondary">Volver a Inicio</a>
            <a href="logout?redirectUrl=loginUsuario.jsp" class="btn btn-outline-primary">
                Cerrar Sesión
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>