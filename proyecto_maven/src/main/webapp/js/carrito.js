class ProductoCarrito {

    constructor(codigo, nombreAlbum, descripcion, imagen, cantidad, precio, existencias) {
        this.codigo = codigo; // Identificador único del producto
        this.nombreAlbum = nombreAlbum;
        this.descripcion = descripcion; // Descripción del producto
        this.imagen = imagen; // Fichero de la imagen
        this.cantidad = cantidad; // Cantidad que tenemos en el carrito (1 por ejemplo)
        this.precio = precio; // Precio unitario del producto
        this.existencias = existencias; // Existencias del producto
    }
}

class Carrito {
    constructor() {
        this.productos = null;
        this.cargarCarrito();
    }

    agregarProducto(producto) {
        if (this.productos != null) {
            const index = this.productos.findIndex(p => p.codigo === producto.codigo);
            if (index !== -1) {
                const nuevaCantidad = this.productos[index].cantidad + producto.cantidad;
                if (nuevaCantidad <= this.productos[index].existencias) {
                    this.productos[index].cantidad = nuevaCantidad;
                    alert("Producto añadido al carrito");
                    console.log(0);
                } else {
                    alert("No hay suficientes existencias para este producto.");
                    console.log(this.productos[index].cantidad);
                    console.log(this.productos[index].existencias);
                    console.log("1");
                }
            } else {
                if (producto.cantidad <= producto.existencias) {
                    this.productos.push(producto);
                    alert("Producto añadido al carrito");
                } else {
                    alert("No hay suficientes existencias para este producto.");
                    console.log(producto.existencias);
                    console.log(producto.cantidad);
                    console.log("klk");
                    console.log("2");
                }
            }
        } else {
            console.log("carrito no ha se cargado o inicializado")
        }
        this.guardarCarrito();
        renderizarCarrito();
        location.reload();
        console.log(this.listarProductos());
    }

    eliminarProducto(codigo) {
        this.productos = this.productos.filter(p => p.codigo !== codigo);
        this.guardarCarrito();
        location.reload();
        renderizarCarrito();
        console.log(this.listarProductos());
    }

    obtenerTotal() {
        return this.productos.reduce((total, p) => total + p.precio * p.cantidad, 0);
    }

    listarProductos() {
        return this.productos.map(p => `${p.cantidad}x ${p.nombreAlbum} - $${p.precio * p.cantidad}`).join('\n');
    }

    cargarCarrito() {
        if (this.productos === null) { // Si no hemos cargado todavía el carrito
            // Cargamos el carrito almacenado
            let data = JSON.parse(localStorage.getItem("mi-carrito-almacenado"));
            console.log("tamo aqui");
            console.log(data, Array.isArray(data));

            if (data === null) { // Si no existía carrito almacenado
                this.productos = []; // Creamos un vector vacío
                console.log("tamo aqui2");
            } else {
                // Extraer el array correctamente
                this.productos = data.productos || [];
                console.log("this.productos es un array:" + Array.isArray(this.productos));
                console.log(this.productos);
            }
        }
    }

    guardarCarrito() {
        localStorage.setItem("mi-carrito-almacenado", JSON.stringify({ productos: this.productos }));
    }

    borrarCarrito() {
        this.productos = [];
        this.guardarCarrito();
        renderizarCarrito();
        location.reload();
    }


}



let carrito = new Carrito();

function updateQuantity(productId, change) {
    let productIndex = carrito.productos.findIndex(p => p.codigo == productId);

    if (productIndex !== -1) {
        let product = carrito.productos[productIndex];
        let newQuantity = Math.max(product.cantidad + change, 1); // Prevent quantity from going below 1

        if (newQuantity <= product.existencias) {
            carrito.productos[productIndex].cantidad = newQuantity;
            carrito.guardarCarrito(); // Save updated cart
            document.querySelector(`[cantidad-product-id="${productId}"]`).textContent = newQuantity;
        } else {
            alert("No hay suficientes existencias para este producto.");
        }
    }

    actualizarTotalCarrito()
    location.reload();
}

// Attach event listeners to the +/- buttons
document.querySelectorAll(".cart-item-quantity button").forEach(button => {
    button.addEventListener("click", function () {
        let isIncrease = this.textContent.trim() === "+";
        let productId = this.parentElement.querySelector("[cantidad-product-id]").getAttribute("cantidad-product-id");
        updateQuantity(productId, isIncrease ? 1 : -1);
    });
});

function renderizarCarrito() {
    const cartContainer = document.getElementById("cartContainer");
    const cartItems = document.getElementById("cartItems");
    const cartFooter = document.getElementById("cartFooter");
    const cartTotal = document.getElementById("cartTotal");

    // Limpiar la lista de productos antes de volver a generarlos
    cartItems.innerHTML = "";

    if (carrito.productos.length === 0) {
        cartContainer.style.display = "none"; // Oculta el carrito si está vacío
    } else {
        cartContainer.style.display = "block"; // Muestra el carrito si tiene productos

        carrito.productos.forEach(producto => {
            const itemHTML = `
                <div class="cart-item">
                    <img src="${producto.imagen}" alt="${producto.nombreAlbum}" class="cart-item-img">
                    <div class="cart-item-details">
                        <h5>Álbum: ${producto.nombreAlbum}</h5>
                        <p>Precio: $${producto.precio.toFixed(2)}</p>
                        <div class="cart-item-quantity">
                            <button class="btn btn-secondary btn-sm" onclick="updateQuantity(${producto.codigo}, -1)">-</button>
                            <span cantidad-product-id="${producto.codigo}">${producto.cantidad}</span>
                            <button class="btn btn-secondary btn-sm" onclick="updateQuantity(${producto.codigo}, 1)">+</button>
                        </div>
                        <button class="btn btn-danger btn-sm mt-2" onclick="carrito.eliminarProducto(${producto.codigo})">Eliminar</button>
                    </div>
                </div>
            `;
            cartItems.innerHTML += itemHTML;
        });

        // Actualizar total
        cartTotal.innerHTML = `<p><strong>Total: ${carrito.obtenerTotal().toFixed(2)} €</strong></p>`;
    }
    // location.reload();

}


document.addEventListener("DOMContentLoaded", function () {
    console.log("Página cargada");


});

function actualizarTotalCarrito() {
    const cartTotal = document.getElementById("cartTotal");
    cartTotal.innerHTML = `<p><strong>Total: ${carrito.obtenerTotal().toFixed(2)} €</strong></p>`;
}

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
            <title>Pedido</title>
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
            <h2>Pedido</h2>
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
            <button id="comprar">Formalizar pedido</button>
            <script>
                // Incluye aquí la función necesaria o referencia al script principal
                let carrito = JSON.parse(localStorage.getItem("mi-carrito-almacenado"));

                document.getElementById('comprar').addEventListener('click', function() {
                    window.opener.EnviarCarrito('/proyecto_maven/procesarPedido', carrito.productos);
                    window.close();
                });
            </script>
        </body>
        </html>
    `;

    ventanaTicket.document.write(contenido);
    ventanaTicket.document.close();
    location.reload();
}

if (document.body.id === "carrito") {
    console.log("Estamos en la página del carrito");
    renderizarCarrito();
} else {
    console.log("No estamos en la página del carrito");
}

function EnviarCarrito(url, carrito) {
    // Convertir el carrito a JSON
    console.log(carrito);
    const carritoJSON = JSON.stringify(carrito);
    
    // Enviar datos al servlet mediante POST
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: carritoJSON,
        credentials: 'include' // Important for session cookies
    })
    .then(response => {
        // First check if response is OK (status 200-299)
        if (!response.ok) {
            // Read the error response text before throwing
            return response.text().then(errorText => {
                throw new Error(`Server responded with status ${response.status}: ${errorText}`);
            });
        }
        // If response is OK, return the text
        return response.text();
    })
    .then(responseText => {
        // Now we have either the redirect URL or error message
        console.log("Server response:", responseText);
        
        // Basic validation of the response
        if (responseText.startsWith("/") || responseText.startsWith("http")) {
            window.location.href = responseText;
        } else {
            throw new Error("Invalid redirect URL received from server");
        }
    })
    .catch(error => {
        console.error('Full error details:', error);
        alert('Error al procesar el pedido: ' + error.message);
        
        // Fallback redirect if something went wrong
        window.location.href = '/proyecto_maven/carrito.html';
    });
}