// Mostrar la tabla de las 25 acciones más rentables de 2024
function cargarAccionesSelector() {
    const select = document.getElementById('accionesFondo');
    if (!select) {
        console.error('No se encontró el selector de acciones (id="accionesFondo")');
        return;
    }
    // Limpia el selector antes de rellenar
    select.innerHTML = '';
    fetch('/acciones_all.json')
        .then(res => res.json())
        .then(acciones => {
            if (!acciones || acciones.length === 0) {
                const opt = document.createElement('option');
                opt.value = '';
                opt.disabled = true;
                opt.textContent = 'No hay acciones disponibles';
                select.appendChild(opt);
                return;
            }
            acciones.forEach(accion => {
                const opt = document.createElement('option');
                opt.value = accion.nombre;
                opt.textContent = accion.nombre;
                select.appendChild(opt);
            });
        })
        .catch(() => {
            const opt = document.createElement('option');
            opt.value = '';
            opt.disabled = true;
            opt.textContent = 'Error al cargar acciones';
            select.appendChild(opt);
        });
}

// Siempre cargar el selector al cargar la página
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        cargarAccionesSelector();
        mostrarTablaAccionesDisponibles();
    });
} else {
    cargarAccionesSelector();
    mostrarTablaAccionesDisponibles();
}

function mostrarTablaAccionesDisponibles() {
    fetch('/acciones_all.json')
        .then(res => res.json())
        .then(acciones => {
            const cont = document.getElementById('tabla-acciones-disponibles');
            if (!cont) return;
            if (!acciones || acciones.length === 0) {
                cont.innerHTML = '<div class="alert alert-warning">No hay acciones disponibles.</div>';
                return;
            }
            // Detecta columnas dinámicamente
            const cols = Object.keys(acciones[0]);
            let tabla = '<div class="table-responsive"><table class="table table-striped"><thead><tr>';
            cols.forEach(col => {
                tabla += `<th>${col.charAt(0).toUpperCase() + col.slice(1)}</th>`;
            });
            tabla += '</tr></thead><tbody>';
            acciones.forEach(accion => {
                tabla += '<tr>';
                cols.forEach(col => {
                    tabla += `<td>${accion[col] ?? ''}</td>`;
                });
                tabla += '</tr>';
            });
            tabla += '</tbody></table></div>';
            cont.innerHTML = tabla;
        })
        .catch(() => {
            const cont = document.getElementById('tabla-acciones-disponibles');
            if (cont) cont.innerHTML = '<div class="alert alert-danger">No se pudo cargar el listado de acciones.</div>';
        });
}





// Envío del formulario de fondos indexados
const formFondos = document.getElementById('formFondos');
if (formFondos) {
    formFondos.addEventListener('submit', function(e) {
        e.preventDefault();
        // Limpiar mensajes previos
        mostrarMensajeFondo('', '');

        const nombre = document.getElementById('nombre').value.trim();
        const cantidad = document.getElementById('cantidad').value;
        const tipo = document.querySelector('input[name="tipo"]:checked')?.value || '';
        const acepta = document.getElementById('acepta').checked;
        const pais = document.getElementById('pais').value;
        const accionesSel = Array.from(document.getElementById('accionesFondo').selectedOptions).map(opt => opt.value);
        if (!nombre || !cantidad || !tipo || !acepta || !pais || accionesSel.length === 0) {
            mostrarMensajeFondo('Todos los campos son obligatorios y debes aceptar los términos.', 'danger');
            return;
        }
        fetch('/api/fondos-indexados', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ nombre, cantidad, tipo, acepta, pais, acciones: accionesSel })
        })
        .then(res => res.json())
        .then(data => {
            mostrarMensajeFondo('¡Fondo guardado correctamente!', 'success');
            formFondos.reset();
            // Añadir el nuevo fondo a la tabla de fondos públicos
            agregarFondoATabla({ nombre, cantidad, tipo, pais, acciones: accionesSel });
        })
        .catch(() => mostrarMensajeFondo('Error al guardar el fondo.', 'danger'));
    });
}

function mostrarMensajeFondo(msg, tipo) {
    const div = document.getElementById('mensajeFondoIndexado');
    if (div) {
        if (!msg) {
            div.innerHTML = '';
        } else {
            div.innerHTML = `<div class="alert alert-${tipo}">${msg}</div>`;
        }
    }
}

// Añade el nuevo fondo a la tabla de fondos públicos
function agregarFondoATabla(fondo) {
    const tabla = document.querySelector('table.table-bordered tbody');
    if (!tabla) return;
    const tr = document.createElement('tr');
    tr.innerHTML = `
        <td>${fondo.nombre}</td>
        <td>${fondo.cantidad}</td>
        <td>${fondo.tipo}</td>
        <td>${fondo.pais}</td>
        <td>
            <span>${Array.isArray(fondo.acciones) ? fondo.acciones.join(', ') : fondo.acciones}</span>
            <ul class="mb-0" style="list-style: none; padding-left: 0;">
                ${(Array.isArray(fondo.acciones) ? fondo.acciones : [fondo.acciones]).map(acc => `<li>${acc}</li>`).join('')}
            </ul>
        </td>
    `;
    tabla.appendChild(tr);
}


    // Tabla de acciones rentables
    fetch('/acciones2024.json')
        .then(res => res.ok ? res.json() : fetch('acciones2024.json').then(r=>r.json()))
        .then(data => mostrarAccionesRentables(data))
        .catch(() => mostrarAccionesRentables([]));

    // Formulario de registro de fondos
    var form = document.getElementById('formFondos');
    if (form) {
        form.addEventListener('submit', enviarFormularioFondos);
    }
    var paisSel = document.getElementById('pais');
    if (paisSel) {
        paisSel.addEventListener('change', function() {
            document.getElementById('paisSeleccionado').textContent = this.value;
        });
    }

function mostrarAccionesRentables(acciones) {
    const contenedor = document.getElementById('tabla-acciones-rentables');
    if (!contenedor) return;
    if (!acciones.length) {
        contenedor.innerHTML = '<div class="alert alert-warning">No se pudieron cargar las acciones más rentables.</div>';
        return;
    }
    let tabla = `<div class="table-responsive"><table class="table table-striped fondos-table"><thead><tr><th>#</th><th>Acción</th><th>Rentabilidad</th><th>Sector</th></tr></thead><tbody>`;
    acciones.forEach((accion, i) => {
        tabla += `<tr><td>${i+1}</td><td>${accion.nombre}</td><td>${accion.rentabilidad}</td><td>${accion.sector}</td></tr>`;
    });
    tabla += '</tbody></table></div>';
    contenedor.innerHTML = tabla;
}

function validarFormularioFondos() {
    let valido = true;
    let mensajes = [];
    const nombre = document.getElementById('nombre').value.trim();
    const cantidad = document.getElementById('cantidad').value;
    const tipo = document.querySelector('input[name="tipo"]:checked');
    const acepta = document.getElementById('acepta').checked;
    const pais = document.getElementById('pais').value;

    if (nombre === '') {
        valido = false;
        mensajes.push('El nombre es obligatorio');
    }
    if (!cantidad || isNaN(cantidad) || cantidad <= 0) {
        valido = false;
        mensajes.push('La cantidad debe ser un número positivo');
    }
    if (!tipo) {
        valido = false;
        mensajes.push('Selecciona un tipo de fondo');
    }
    if (!acepta) {
        valido = false;
        mensajes.push('Debes aceptar los términos');
    }
    if (pais === '') {
        valido = false;
        mensajes.push('Selecciona un país');
    }

    if (!valido) {
        Swal.fire({
            icon: 'error',
            title: 'Error en el formulario',
            html: mensajes.join('<br>'),
        });
    }
    return valido;
}

function enviarFormularioFondos(event) {
    event.preventDefault();
    if (!validarFormularioFondos()) return;

    const data = {
        nombre: document.getElementById('nombre').value,
        cantidad: document.getElementById('cantidad').value,
        tipo: document.querySelector('input[name="tipo"]:checked').value,
        acepta: document.getElementById('acepta').checked,
        pais: document.getElementById('pais').value
    };

    fetch('/api/fondos', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(respuesta => {
        Swal.fire({
            icon: 'success',
            title: 'Fondos enviados',
            text: respuesta.mensaje || 'Datos registrados correctamente.'
        });
        document.getElementById('formFondos').reset();
    })
    .catch(() => {
        Swal.fire({
            icon: 'error',
            title: 'Error de red',
            text: 'No se pudo enviar la información.'
        });
    });
}
