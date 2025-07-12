<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList, prueba.Producto" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Pedido Finalizado</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="stylesheets/pedidoFinal.css">
</head>
<body>
    <div class="container">
        <div class="confirmation-card">
            <div class="confirmation-icon">✓</div>
            <h1>¡Pedido Completado!</h1>
            <p>Tu pedido ha sido procesado correctamente.</p>
            
            <div class="order-number">
                Número de pedido: ${sessionScope.codigoPedido}
            </div>
            
            <p>Recibirás un correo de confirmación con los detalles de tu compra.</p>
            
            <a href="index.jsp" class="btn btn-primary mt-3">Volver a la tienda</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        localStorage.removeItem('mi-carrito-almacenado');
        sessionStorage.removeItem(mi-carrito-almacenado);
    </script>
    
    <% 
        // Limpiar carrito de la sesión
        session.removeAttribute("mi-carrito-almacenado");
    %>
</body>
</html>