// validacionRegistro.js

document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form[th\\:action="@{/registro}"]') || document.querySelector('form[action="/registro"]');
    if (!form) return;

    // Limitar teléfono a 9 caracteres y solo números en tiempo real
    const telefono = document.getElementById('telefono');
    telefono.addEventListener('input', function (e) {
        // Eliminar caracteres no numéricos
        this.value = this.value.replace(/[^0-9]/g, '');
        // Limitar a 9 caracteres
        if (this.value.length > 9) {
            this.value = this.value.slice(0, 9);
        }
    });

    form.addEventListener('submit', async function (e) {
        e.preventDefault(); // Siempre prevenir por defecto, solo enviar si todo está ok
        let valid = true;
        let mensajes = [];

        // Usuario
        const username = document.getElementById('username');
        const usernameValue = username.value.trim();
        if (!usernameValue) {
            valid = false;
            mensajes.push('El nombre de usuario es obligatorio.');
        } else if (usernameValue.length < 3) {
            valid = false;
            mensajes.push('El nombre de usuario debe tener al menos 3 caracteres.');
        } else {
            // Validación AJAX al backend
            try {
                const resp = await fetch(`/api/usuarios/existe?username=${encodeURIComponent(usernameValue)}`);
                if (resp.ok) {
                    const existe = await resp.json();
                    if (existe === true) {
                        valid = false;
                        mensajes.push('Nombre ya en uso.');
                    }
                } else {
                    valid = false;
                    mensajes.push('Error comprobando usuario.');
                }
            } catch (error) {
                valid = false;
                mensajes.push('Error comprobando usuario.');
            }
        }

        // Teléfono
        const telefonoPattern = /^[0-9]{9}$/;
        if (!telefono.value.trim()) {
            valid = false;
            mensajes.push('El teléfono es obligatorio.');
        } else if (!telefonoPattern.test(telefono.value.trim())) {
            valid = false;
            mensajes.push('El teléfono debe tener exactamente 9 dígitos numéricos.');
        }

        // Email
        const email = document.getElementById('email');
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email.value.trim()) {
            valid = false;
            mensajes.push('El email es obligatorio.');
        } else if (!emailPattern.test(email.value.trim())) {
            valid = false;
            mensajes.push('El email no tiene un formato válido.');
        }

        // Contraseña
        const password = document.getElementById('password');
        if (!password.value.trim()) {
            valid = false;
            mensajes.push('La contraseña es obligatoria.');
        } else if (password.value.trim().length < 6) {
            valid = false;
            mensajes.push('La contraseña debe tener al menos 6 caracteres.');
        }

        // Fecha de nacimiento
        const fecha = document.getElementById('fecha');
        if (!fecha.value) {
            valid = false;
            mensajes.push('La fecha de nacimiento es obligatoria.');
        }

        // Mostrar mensajes de error
        let errorDiv = document.getElementById('registro-errores');
        if (!errorDiv) {
            errorDiv = document.createElement('div');
            errorDiv.id = 'registro-errores';
            errorDiv.className = 'alert alert-danger mt-3';
            form.parentNode.insertBefore(errorDiv, form);
        }

        if (!valid) {
            errorDiv.innerHTML = mensajes.map(msg => `<div>${msg}</div>`).join('');
            errorDiv.style.display = 'block';
        } else {
            errorDiv.style.display = 'none';
            form.submit(); // Solo enviar si todo es válido
        }
    });
});
