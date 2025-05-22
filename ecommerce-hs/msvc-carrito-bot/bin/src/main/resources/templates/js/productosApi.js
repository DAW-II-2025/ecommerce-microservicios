const BASE_URL = "http://localhost:8081/Producto/list"; // Asegura que la URL incluya el protocolo

$(document).ready(function() {
    $.ajax({
        url: BASE_URL,
        method: 'GET',
        success: function(response) {
            console.log('Response:', response); // Imprime la respuesta en la consola
            let data = response; // La respuesta ya es un objeto, no necesitas parsearla

            if (Array.isArray(data)) {
                let productosContainer = $('#productos-container');
                productosContainer.empty(); // Limpiar cualquier contenido existente

                data.forEach(function(producto) {
                    let productHtml = 
                        `
                        <div class="carts">
                            <div>
                                <h4 id="idProducto" style="display: none">${producto.idProducto}</h4>
                                <img src="${producto.imagen}" alt="">
                                <p>S/.<span>${producto.precioUnidad}</span></p>
                            </div>
                            <p class="title">${producto.descripcion}</p>
                            <a href="" class="btn-agregar-carrito" data-id="${producto.idProducto}">Agregar</a>
                        </div>`
                    ;
                    productosContainer.append(productHtml);
                });
            } else {
                console.error('Unexpected response format:', data);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching productos:', error);
        }
    });
});