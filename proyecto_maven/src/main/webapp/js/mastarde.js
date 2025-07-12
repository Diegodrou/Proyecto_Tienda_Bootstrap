function generarTicket() {
    if (carrito.productos.length === 0) {
        alert("El carrito está vacío. No se puede generar un ticket.");
        return;
    }

    let ventanaTicket = window.open("", "_blank");
    let fecha = new Date().toLocaleString();

    let contenido = `
        <html>
        <head>
            <title>Ticket de Compra</title>
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
        </head>
        <body>
            <h2>Ticket de Compra</h2>
            <p class="fecha">Fecha: ${fecha}</p>
            <table>
                <tr>
                    <th>Producto</th>
                    <th>Cantidad</th>
                    <th>Precio Unitario</th>
                    <th>Subtotal</th>
                </tr>
    `;

    carrito.productos.forEach(producto => {
        let subtotal = producto.precio * producto.cantidad;
        contenido += `
            <tr>
                <td>${producto.nombreAlbum}</td>
                <td>${producto.cantidad}</td>
                <td>$${producto.precio.toFixed(2)}</td>
                <td>$${subtotal.toFixed(2)}</td>
            </tr>
        `;
    });

    contenido += `
            </table>
            <p class="total">Total: $${carrito.obtenerTotal().toFixed(2)}</p>
            <button onclick="window.print()">Imprimir Ticket</button>
        </body>
        </html>
    `;

    ventanaTicket.document.write(contenido);
    ventanaTicket.document.close();
    location.reload();
}