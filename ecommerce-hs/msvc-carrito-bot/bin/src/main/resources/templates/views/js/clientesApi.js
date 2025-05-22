const BASE_URL = "http://localhost:8081/Cliente/add"; // Asegura que la URL incluya el protocolo

$(document).ready(function() {
    $('.btn-primary').click(function() {
        var firstName = $('#firstName').val();
        var lastName = $('#lastName').val();
        var address = $('#address').val();
        var birthDate = $('#birthDate').val();
        var gender = $('input[name="gender"]:checked').val();
        var email = $('#email').val();
        var password = $('#password').val();

        var cliente = {
            nombres: firstName,
            apellidos: lastName,
            direccion: address,
            fechaNacimiento: birthDate,
            sexo: gender,
            correo: email,
            password: password,
        };

        $.ajax({
            type: 'POST',
            url: BASE_URL,
            contentType: 'application/json',
            data: JSON.stringify(cliente),
            success: function(response) {
                alert('Cliente registrado:', response);
                window.location.href = 'login.html';
                // Aquí podrías manejar la respuesta de la API como desees
                // Por ejemplo, mostrar un mensaje de éxito o redirigir a otra página
            },
            error: function(error) {
                alert('Error al registrar cliente:', error);
                // Manejo de errores, por ejemplo mostrar un mensaje al usuario
            }
        });
    });
});