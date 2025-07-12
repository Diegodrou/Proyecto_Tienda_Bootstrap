<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.List,prueba.*" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de productos</title>
</head>
<body>
    <%
	int categoria = 1;
	AccesoBD con=AccesoBD.getInstance();
	List<ProductoBD> productos = con.obtenerProductosBD();
    %>
	<div>
		<table>
			<tr>
				<th>&nbsp;</th>
				<th>Descripción</th>
				<th>Precio</th>
				<th>&nbsp;</th>
			</tr>
			<% for (ProductoBD producto : productos){
				int codigo=producto.getCodigo();
				String descripcion=producto.getDescripcion();
				float precio=producto.getPrecio();
				int existencias=producto.getStock();
				String imagen=producto.getImagen();
			%>
			<tr>
				<td><img src="img/<%=imagen%>" alt="<%=descripcion%>" width="400" 
                    height="500"></td>
				<td><%=descripcion%></td>
				<td><%=precio%></td>
				<td>
					<% if (existencias > 0) {
					%>
						<input type="button" value="Añadir al carrito" onclick="anadirCarrito(<%=codigo%>,'<%=descripcion%>','img/<%=imagen%>',<%=precio%>,<%=existencias%>)">
					<% } else { %>
						&nbsp;
					<% } %>
				</td>
			</tr>
			<% } %>
		</table>
	</div>
</body>
</html>