function EnviarCarrito(url, carrito) {
    // Convertir el carrito a JSON
    const carritoJSON = JSON.stringify(carrito);
    
    // Enviar datos al servlet mediante POST
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: carritoJSON
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        return response.text(); // o .json() si el servlet devuelve JSON
    })
    .then(data => {
        // Redirigir a la página de confirmación que devuelve el servlet
        window.location.href = data;
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error al procesar el pedido: ' + error.message);
    });
}

let carrito = new Carrito();
