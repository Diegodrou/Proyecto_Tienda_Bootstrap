<%@page contentType="Text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <title>VinLo</title>
        <link rel="icon" type="image/ico" href="img/logo.png" sizes="64x64">
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="stylesheets/principal.css">
    </head>

    <body>
        <div class="video-container">
            <video autoplay loop muted>
                <source src="vids/site-video.mp4" type="video/mp4">
                Your browser does not support the video tag.
            </video>
        </div>
        <mi-menu></mi-menu>
        <div class="header-text">Sonido analógico, emociones orgánicas</div>
        <!-- Company Description Section -->
        <div id="company-description" class="container mt-5 hidden">
            <h2>VinLo</h2>
            <p>VinLo es una tienda especializada en vinilos, ofreciendo una selección curada de discos clásicos y
                modernos
                para los amantes del sonido auténtico. Nos apasiona la música y queremos compartir esa pasión contigo.
            </p>
            <div class="image-container">
                <img src="img/imagen-vinilo.jpeg" alt="Vinilo">
            </div>
        </div>

        <mi-pie></mi-pie>
        <script src="./js/index.js"></script>
        <script src="./js/mis-etiquetas.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    </body>

    </html>