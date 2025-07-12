<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.List, prueba.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Productos - VinLo</title>
    <link rel="icon" type="image/ico" href="img/logo.png" sizes="64x64">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="stylesheets/prin_3.css">
</head>

<body>
    <mi-menu></mi-menu>
    <div class="content">
        <h2>Compra tus discos favoritos!</h2>

        <div class="row row-cols-1 row-cols-md-3 g-4">
            <%
                AccesoBD con = AccesoBD.getInstance();
                List<ProductoBD> productos = con.obtenerProductosBD();
                for (ProductoBD producto : productos) {
                    int codigo = producto.getCodigo();
                    String descripcion = producto.getDescripcion(); // Usada como artista
                    float precio = producto.getPrecio();
                    int stock = producto.getStock();
                    String imagen = producto.getImagen();
                    String nomnbre_album = producto.getNombreAlbum();
            %>
            <div class="col">
                <div class="card album-card">
                    <img src="img/<%= imagen %>" class="card-img-top" alt="<%= descripcion %>">
                    <div class="card-body">
                        <h5 class="card-title"><%= nomnbre_album %></h5>
                        <p class="card-text"><%= descripcion %></p>
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="price">$<%= String.format("%.2f", precio) %></span>
                            <% if (stock > 0) { %>
                                <button class="btn btn-primary"
                                    onclick="carrito.agregarProducto(new ProductoCarrito(<%= codigo %>, '<%= nomnbre_album %>', '<%=descripcion%>', 'img/<%= imagen %>', 1, <%= precio %>, <%= stock %>))">Comprar</button>
                            <% } else { %>
                                <button class="btn btn-secondary" disabled>Agotado</button>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </div>

    <mi-pie></mi-pie>
    <script src="./js/mis-etiquetas.js"></script>
    <script src="./js/carrito.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
