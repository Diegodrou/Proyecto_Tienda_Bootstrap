<%@page language="java" contentType="text/html;charset=UTF-8" import="prueba.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Iniciar Sesión - VinLo</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="stylesheets/usuario.css">
</head>
<body>
	<%-- Utilizamos una variable en la sesión para informar de los mensajes de Error --%>
	<%
	String mensaje = (String)session.getAttribute("mensaje");

	if (mensaje != null) {
	%>
	<%-- Eliminamos el mensaje consumido --%>
	<%
		session.removeAttribute("mensaje");
	%>
	<h1> <%=mensaje%> </h1>
	<%
	}
	%>

	<%-- Si no hay código de usuario o no es válido --%>

	<%
	if ((session.getAttribute("codigo") == null) ||
	    ((Integer)session.getAttribute("codigo") <=0 ))
	{
	%>
	<%-- Mostramos el formulario para la introducción del usuario y la clave --%>

	<div class="container">
        <h2 class="text-center mt-5">Iniciar Sesión</h2>
        <form action="login" method="post" class="mt-4">
            <input type="hidden" name="url" value="./loginUsuario.jsp">
            <div class="mb-3">
                <label for="usuario" class="form-label">Nombre de Usuario</label>
                <input type="text" class="form-control" id="usuario" name="usuario" required>
            </div>
            <div class="mb-3">
                <label for="clave" class="form-label">Contraseña</label>
                <input type="password" class="form-control" id="clave" name="clave" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Iniciar Sesión</button>
        </form>
		<div class="mb-3" style="margin-top: 5px;">
			<button onclick="window.location.href='registroUsuario.jsp'" class="btn-primary" >Registrarse</button>
		</div>
    </div>
	<%
	} else {
		// Si existe un usuario, se redirige a la página que toca, por ejemplo compra.jsp, aunque podría ser las preferencias del usuario.
		response.sendRedirect("usuario.jsp");
	}
	%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>