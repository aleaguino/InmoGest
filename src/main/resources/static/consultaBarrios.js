// Este script hace una petición a la API de Geoapify Places para consultar datos de barrio
// Necesitas tu propia API Key gratuita de https://www.geoapify.com/places-api/

function consultaBarrio(ciudad, direccion) {
    const apiKey = "61cb7e05097e4bb890214efee4b7e8c9"; // <-- API Key proporcionada por el usuario
    let intento = 1;
    let queryFull = direccion ? `${direccion}, ${ciudad}, España` : `${ciudad}, España`;
    let querySimple = `${ciudad}, España`;
    let url = `https://api.geoapify.com/v1/geocode/search?text=${encodeURIComponent(queryFull)}&lang=es&apiKey=${apiKey}`;

    fetch(url)
        .then(resp => resp.json())
        .then(data => {
            if (data.features && data.features.length > 0) {
                mostrarResultado(data, intento);
            } else if (direccion) {
                // Si falla con dirección, intenta solo ciudad
                intento = 2;
                let url2 = `https://api.geoapify.com/v1/geocode/search?text=${encodeURIComponent(querySimple)}&lang=es&apiKey=${apiKey}`;
                fetch(url2)
                    .then(resp2 => resp2.json())
                    .then(data2 => {
                        if (data2.features && data2.features.length > 0) {
                            mostrarResultado(data2, intento);
                        } else {
                            document.getElementById('resultadoBarrio').innerHTML = 'No se encontraron datos para esa dirección ni para la ciudad.<br><pre>' + JSON.stringify(data2, null, 2) + '</pre>';
                        }
                    })
                    .catch(() => {
                        document.getElementById('resultadoBarrio').innerHTML = 'Error al consultar la API (ciudad).';
                    });
            } else {
                document.getElementById('resultadoBarrio').innerHTML = 'No se encontraron datos para esa dirección.<br><pre>' + JSON.stringify(data, null, 2) + '</pre>';
            }
        })
        .catch(() => {
            document.getElementById('resultadoBarrio').innerHTML = 'Error al consultar la API.';
        });
}

function mostrarResultado(data, intento) {
    const props = data.features[0].properties;
    const barrio = props.suburb || props.city || 'No disponible';
    const municipio = props.city || 'No disponible';
    const postal = props.postcode || 'No disponible';
    const provincia = props.county || 'No disponible';
    const comunidad = props.state || 'No disponible';
    const lat = data.features[0].geometry.coordinates[1];
    const lon = data.features[0].geometry.coordinates[0];
    const zona = props.timezone ? props.timezone.name : 'No disponible';
    const zonaAbbr = props.timezone ? (props.timezone.abbreviation_STD + (props.timezone.abbreviation_DST ? '/' + props.timezone.abbreviation_DST : '')) : '';
    const plusCode = props.plus_code_short || props.plus_code || 'No disponible';
    const tipoZona = props.category || props.result_type || 'No disponible';
    const confianza = props.rank && props.rank.confidence ? props.rank.confidence : 'No disponible';
    const popularidad = props.rank && props.rank.popularity ? props.rank.popularity.toFixed(2) : 'No disponible';
    const area = props.bbox ? `(${(Math.abs(props.bbox[2] - props.bbox[0]) * Math.abs(props.bbox[3] - props.bbox[1])).toFixed(3)} grados²)` : 'No disponible';
    let info = `<h4>${barrio}</h4>
        <ul>
            <li><b>Ciudad:</b> ${municipio}</li>
            <li><b>Provincia:</b> ${provincia}</li>
            <li><b>Comunidad Autónoma:</b> ${comunidad}</li>
            <li><b>Código Postal:</b> ${postal}</li>
            <li><b>Coordenadas:</b> ${lat}, ${lon} <button class='btn btn-sm btn-outline-secondary' onclick='navigator.clipboard.writeText("${lat},${lon}")'>Copiar</button></li>
            <li><b>Plus Code:</b> ${plusCode}</li>
            <li><b>Tipo de zona:</b> ${tipoZona}</li>
            <li><b>Popularidad:</b> ${popularidad}</li>
            <li><b>Confianza API:</b> ${confianza}</li>
            <li><b>Área aproximada:</b> ${area}</li>
            <li><b>Zona horaria:</b> ${zona} (${zonaAbbr})</li>
            <li><b>Ver en <a href="https://www.openstreetmap.org/?mlat=${lat}&mlon=${lon}#map=15/${lat}/${lon}" target="_blank">OpenStreetMap</a></b></li>
        </ul>
        <div id="poiBarrio" class="mb-2"></div>`;
    if (intento === 2) info = '<b>(Resultado usando solo ciudad)</b><br>' + info;
    // Añadir el div para el mapa si no existe
    if (!document.getElementById('mapaBarrio')) {
        const mapaDiv = document.createElement('div');
        mapaDiv.id = 'mapaBarrio';
        mapaDiv.style.height = '300px';
        mapaDiv.style.marginBottom = '1em';
        document.getElementById('resultadoBarrio').innerHTML = info;
        document.getElementById('resultadoBarrio').appendChild(mapaDiv);
    } else {
        document.getElementById('resultadoBarrio').innerHTML = info;
    }
    setTimeout(() => {
        if (window.L && document.getElementById('mapaBarrio')) {
            document.getElementById('mapaBarrio').innerHTML = "";
            const map = L.map('mapaBarrio').setView([lat, lon], 15);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(map);
            L.marker([lat, lon]).addTo(map)
                .bindPopup(`<b>${barrio}</b><br>${municipio}`).openPopup();
            buscarPOIs(lat, lon, map);
        }
    }, 200);
}

function buscarPOIs(lat, lon, map) {
    const apiKey = "61cb7e05097e4bb890214efee4b7e8c9";
    const categories = [
        "education.school",
        "education.college",
        "education.university",
        "healthcare.hospital",
        "healthcare.clinic",
        "healthcare.pharmacy",
        "commercial.supermarket",
        "commercial.grocery",
        "leisure.park",
        "public_transport.stop",
        "public_transport.station",
        "public_transport.subway",
        "commercial.mall",
        "commercial.bank",
        "commercial.atm",
        "commercial.gas_station",
        "commercial.bakery",
        "commercial.restaurant",
        "commercial.cafe",
        "commercial.bar",
        "leisure.fitness_centre",
        "leisure.sports_centre",
        "leisure.museum",
        "leisure.library",
        "service.post_office",
        "service.veterinary"
    ];
    const url = `https://api.geoapify.com/v2/places?categories=${categories.join(",")}&filter=circle:${lon},${lat},2000&limit=30&lang=es&apiKey=${apiKey}`;
    fetch(url)
        .then(resp => resp.json())
        .then(data => {
            let html = '<b>Puntos de interés cercanos (1km):</b><ul>';
            if (data.features && data.features.length > 0) {
                // Resumen de recuento de servicios
                const resumen = {};
                data.features.forEach(f => {
                    const tipo = f.properties.categories[0];
                    resumen[tipo] = (resumen[tipo] || 0) + 1;
                });
                let resumenHtml = '<b>Resumen de servicios encontrados:</b><ul style="columns:2;">';
                Object.keys(resumen).forEach(tipo => {
                    resumenHtml += `<li>${tipo.replace(/\./g,' ')}: <b>${resumen[tipo]}</b></li>`;
                });
                resumenHtml += '</ul>';
                html += resumenHtml;
                // Lista de POIs detallada
                data.features.forEach(f => {
                    const tipo = f.properties.categories[0];
                    const nombre = f.properties.name || tipo.replace(/\./g, ' ');
                    const dist = f.properties.distance ? ` (${Math.round(f.properties.distance)} m)` : '';
                    const direccion = f.properties.address_line1 || '';
                    const telefono = f.properties.phone || '';
                    const web = f.properties.website || '';
                    const horario = f.properties.opening_hours || '';
                    let icono = '🏫';
                    if (tipo.includes('school') || tipo.includes('college') || tipo.includes('university')) icono = '🏫';
                    else if (tipo.includes('hospital') || tipo.includes('clinic')) icono = '🏥';
                    else if (tipo.includes('pharmacy')) icono = '💊';
                    else if (tipo.includes('supermarket') || tipo.includes('grocery')) icono = '🛒';
                    else if (tipo.includes('park')) icono = '🌳';
                    else if (tipo.includes('public_transport')) icono = '🚉';
                    else if (tipo.includes('mall')) icono = '🏬';
                    else if (tipo.includes('bank')) icono = '🏦';
                    else if (tipo.includes('atm')) icono = '🏧';
                    else if (tipo.includes('gas_station')) icono = '⛽';
                    else if (tipo.includes('bakery')) icono = '🥐';
                    else if (tipo.includes('restaurant')) icono = '🍽️';
                    else if (tipo.includes('cafe')) icono = '☕';
                    else if (tipo.includes('bar')) icono = '🍺';
                    else if (tipo.includes('fitness_centre') || tipo.includes('sports_centre')) icono = '🏋️';
                    else if (tipo.includes('museum')) icono = '🏛️';
                    else if (tipo.includes('library')) icono = '📚';
                    else if (tipo.includes('post_office')) icono = '📮';
                    else if (tipo.includes('veterinary')) icono = '🐾';
                    html += `<li style='margin-bottom:0.5em;'>${icono} <b>${nombre}</b> <span style='color:#888'>${tipo.replace(/\./g,' ')}</span>${dist}<br>` +
                        (direccion ? `<span style='color:#555'>${direccion}</span><br>` : '') +
                        (telefono ? `📞 <a href='tel:${telefono}'>${telefono}</a> ` : '') +
                        (web ? `🌐 <a href='${web}' target='_blank'>web</a> ` : '') +
                        (horario ? `🕒 ${horario}` : '') +
                        ` <a href='https://www.google.com/maps/search/?api=1&query=${f.geometry.coordinates[1]},${f.geometry.coordinates[0]}' target='_blank'>Ver en Google Maps</a></li>`;
                    // Añadir marcador al mapa
                    if (window.L && map) {
                        L.marker([f.geometry.coordinates[1], f.geometry.coordinates[0]], {icon: L.icon({iconUrl: null, iconSize:[0,0]})})
                            .addTo(map)
                            .bindPopup(`${icono} <b>${nombre}</b><br>${tipo.replace(/\./g,' ')}${direccion ? '<br>'+direccion : ''}`);
                    }
                });
                html += '</ul>';
            } else {
                html += '<li>No se encontraron puntos de interés cercanos en 2km.<br>Explora la zona en <a href="https://www.google.com/maps/search/?api=1&query=' + lat + ',' + lon + '" target="_blank">Google Maps</a> o <a href="https://www.openstreetmap.org/#map=15/' + lat + '/' + lon + '" target="_blank">OpenStreetMap</a>.</li></ul>';
            }
            document.getElementById('poiBarrio').innerHTML = html;
        })
        .catch(() => {
            document.getElementById('poiBarrio').innerHTML = 'No se pudieron cargar los puntos de interés cercanos.';
        });
}



// Esta función se llama cuando el usuario pulsa el botón
function lanzarConsultaBarrio() {
    // Puedes obtener la ciudad y dirección del primer piso mostrado (o pedirlo al usuario)
    const ciudad = document.querySelector('[data-ciudad]').getAttribute('data-ciudad');
    const direccion = document.querySelector('[data-direccion]').getAttribute('data-direccion');
    consultaBarrio(ciudad, direccion);
}
