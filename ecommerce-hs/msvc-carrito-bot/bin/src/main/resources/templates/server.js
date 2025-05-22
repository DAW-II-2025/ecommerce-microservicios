const express = require('express');
const path = require('path');
const session = require('express-session');
const bodyParser = require('body-parser');
const axios = require('axios');
const app = express();
const router = express.Router();

const urlverificar = "http://localhost:8081/Cliente/verificar";

app.use(bodyParser.urlencoded({ extended: true }));

app.use(session({
    secret: 'tu_secreto',
    resave: false,
    saveUninitialized: true,
    cookie: { secure: false }
}));

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

app.use('/front', express.static(path.join(__dirname, 'views')));

router.get('/front/login', function(req, res) {
    res.render('login');
});

router.post('/front/login', async (req, res) => {
    const { correo, pass } = req.body;

    try {
        const response = await axios.post(urlverificar, { correo, pass });

        if (response.data && response.data.idCliente) {
            req.session.user = {
                id: response.data.idCliente,
                username: `${response.data.nombres}`
            };
            res.redirect('/front/index');
        } else {
            res.send('Credenciales inválidas');
        }
    } catch (error) {
        console.error('Error al verificar las credenciales:', error.message);
        res.status(500).send('Error interno al verificar las credenciales');
    }
});

function authMiddleware(req, res, next) {
    if (req.session.user) {
        next();
    } else {
        res.redirect('/front/login');
    }
}

router.get('/front/index', authMiddleware, (req, res) => {
    res.render('index', { username: req.session.user.username, id: req.session.user.id });
});

router.get('/logout', (req, res) => {
    req.session.destroy((err) => {
        if (err) {
            return res.send('Error al cerrar sesión');
        }
        res.redirect('/front/login');
    });
});

router.get('/front/comprar', authMiddleware, (req, res) => {
    const total = req.query.total || '0';
    const productosJson = req.query.productos || '[]';
    const productos = JSON.parse(productosJson);

    res.render('comprar', {
        username: req.session.user.username,
        total,
        productos,
        id: req.session.user.id
    });
});

router.post('/front/registrar', (req, res) => {
    res.redirect('/front/login');
});

router.get('/front/productos', authMiddleware, (req, res) => {
    res.render('productos', { username: req.session.user.username, id: req.session.user.id });
});

router.get('/front/registro', (req, res) => {
    res.render('registro');
});

router.get('/front/quienessomos', (req, res) => {
    res.render('quienessomos', { username: req.session.user.username, id: req.session.user.id });
});

router.get('/front/verMiInformacion', (req, res) => {
    res.render('verMiInformacion', { username: req.session.user.username, id: req.session.user.id });
});

router.get('/front/verMisPedidos', (req, res) => {
    res.render('verMisPedidos', { username: req.session.user.username, id: req.session.user.id });
});

router.get('/front/verDetallesPedido', (req, res) => {
    res.render('verDetallesPedido', { username: req.session.user.username, id: req.session.user.id });
});

app.use('/', router);

app.listen(3000, () => {
    console.log('Servidor front-end está corriendo en el puerto 3000');
});
