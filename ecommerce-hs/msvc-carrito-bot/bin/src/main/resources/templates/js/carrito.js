// variables
let todosLosContenedoresCarrito = document.querySelector('.products');
let contenedorCompraCarrito = document.querySelector('.card-items');
let precioTotal = document.querySelector('.price-total');
let cantidadProductos = document.querySelector('.count-product');
let botonComprar = document.getElementById('buy-button');

let productosComprados = [];
let totalCarrito = 0;
let cantidadProductosCarrito = 0;

// funciones
cargarEventos();
function cargarEventos(){
    todosLosContenedoresCarrito.addEventListener('click', agregarProducto);
    contenedorCompraCarrito.addEventListener('click', eliminarProducto);
    botonComprar.addEventListener('click', comprarProductos);
}

function agregarProducto(e){
    e.preventDefault();
    if (e.target.classList.contains('btn-agregar-carrito')) {
        const seleccionarProducto = e.target.parentElement; 
        leerContenido(seleccionarProducto);
    }
}

function eliminarProducto(e) {
    if (e.target.classList.contains('eliminar-producto')) {
        const idEliminar = e.target.getAttribute('data-id');

        productosComprados.forEach(valor => {
            if (valor.id == idEliminar) {
                let precioReducido = parseFloat(valor.precio) * parseFloat(valor.cantidad);
                totalCarrito = totalCarrito - precioReducido;
                totalCarrito = totalCarrito.toFixed(2);
            }
        });
        productosComprados = productosComprados.filter(producto => producto.id !== idEliminar);
        
        cantidadProductosCarrito--;
    }
    if (productosComprados.length === 0) {
        precioTotal.innerHTML = 0;
        cantidadProductos.innerHTML = 0;
        botonComprar.disabled = true;
    }
    cargarHtml();
}

function leerContenido(producto){
    const infoProducto = {
        imagen: producto.querySelector('div img').src,
        titulo: producto.querySelector('.title').textContent,
        precio: producto.querySelector('div p span').textContent,
        id: producto.querySelector('a').getAttribute('data-id'),
        cantidad: 1
    }

    totalCarrito = parseFloat(totalCarrito) + parseFloat(infoProducto.precio);
    totalCarrito = totalCarrito.toFixed(2);

    const existe = productosComprados.some(producto => producto.id === infoProducto.id);
    if (existe) {
        const pro = productosComprados.map(producto => {
            if (producto.id === infoProducto.id) {
                producto.cantidad++;
                return producto;
            } else {
                return producto;
            }
        });
        productosComprados = [...pro];
    } else {
        productosComprados = [...productosComprados, infoProducto];
        cantidadProductosCarrito++;
    }
    cargarHtml();
}

function cargarHtml(){
    limpiarHtml();
    productosComprados.forEach(producto => {
        const {imagen, titulo, precio, cantidad, id} = producto;
        const fila = document.createElement('div');
        fila.classList.add('item');
        fila.innerHTML = `
            <img src="${imagen}" alt="">
            <div class="contenido-item">
                <h5>${titulo}</h5>
                <h5 class="precio-carrito">S/.${precio}</h5>
                <h6>Cantidad: ${cantidad}</h6>
            </div>
            <span class="eliminar-producto" data-id="${id}">X</span>
        `;

        contenedorCompraCarrito.appendChild(fila);

        precioTotal.innerHTML = totalCarrito;
        cantidadProductos.innerHTML = cantidadProductosCarrito;
    });

    if (productosComprados.length > 0) {
        botonComprar.disabled = false;
    }
}
function limpiarHtml(){
    contenedorCompraCarrito.innerHTML = '';
}

function comprarProductos() {
    window.location.href = '/front/comprar';
}
