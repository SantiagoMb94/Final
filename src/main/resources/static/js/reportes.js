let empresas = [];
let encuestas = [];

document.addEventListener('DOMContentLoaded', () => {
    cargarEmpresas();
    cargarEncuestas();
});

async function cargarEmpresas() {
    try {
        empresas = await api.get('/empresas?activas=true');
        const selectEmpresa = document.getElementById('selectEmpresaReporte');
        empresas.forEach(empresa => {
            const option = document.createElement('option');
            option.value = empresa.id;
            option.textContent = empresa.nombre;
            selectEmpresa.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar empresas:', error);
    }
}

async function cargarEncuestas() {
    try {
        encuestas = await api.get('/encuestas');
        const selectEncuesta = document.getElementById('selectEncuesta');
        encuestas.forEach(encuesta => {
            const option = document.createElement('option');
            option.value = encuesta.id;
            option.textContent = `${encuesta.titulo} (${obtenerNombreEmpresa(encuesta.empresaId)})`;
            selectEncuesta.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar encuestas:', error);
    }
}

function obtenerNombreEmpresa(empresaId) {
    const empresa = empresas.find(e => e.id === empresaId);
    return empresa ? empresa.nombre : '-';
}

function cambiarTipoReporte() {
    const tipo = document.getElementById('tipoReporte').value;
    const reporteEncuesta = document.getElementById('reporteEncuesta');
    const reporteEmpresa = document.getElementById('reporteEmpresa');

    if (tipo === 'encuesta') {
        reporteEncuesta.style.display = 'block';
        reporteEmpresa.style.display = 'none';
    } else {
        reporteEncuesta.style.display = 'none';
        reporteEmpresa.style.display = 'block';
    }

    document.getElementById('reporteResultado').innerHTML = '';
}

async function generarReporteEncuesta() {
    const encuestaId = document.getElementById('selectEncuesta').value;
    if (!encuestaId) {
        ui.showAlert('Por favor seleccione una encuesta', 'error');
        return;
    }

    const resultado = document.getElementById('reporteResultado');
    ui.showLoading(resultado);

    try {
        const reporte = await api.get(`/reportes/encuestas/${encuestaId}`);
        mostrarReporteEncuesta(reporte);
    } catch (error) {
        resultado.innerHTML = '<div class="alert alert-error">Error al generar reporte: ' + error.message + '</div>';
    }
}

async function generarReporteEmpresa() {
    const empresaId = document.getElementById('selectEmpresaReporte').value;
    if (!empresaId) {
        ui.showAlert('Por favor seleccione una empresa', 'error');
        return;
    }

    const resultado = document.getElementById('reporteResultado');
    ui.showLoading(resultado);

    try {
        const reporte = await api.get(`/reportes/empresas/${empresaId}`);
        mostrarReporteEmpresa(reporte);
    } catch (error) {
        resultado.innerHTML = '<div class="alert alert-error">Error al generar reporte: ' + error.message + '</div>';
    }
}

function mostrarReporteEncuesta(reporte) {
    const resultado = document.getElementById('reporteResultado');
    const encuesta = reporte.encuesta;

    let html = '<div class="card">';
    html += '<h3>Reporte de Encuesta</h3>';
    html += '<div style="margin-bottom: 2rem;">';
    html += `<p><strong>Título:</strong> ${encuesta.titulo}</p>`;
    html += `<p><strong>Descripción:</strong> ${encuesta.descripcion || '-'}</p>`;
    html += `<p><strong>Estado:</strong> ${encuesta.estado}</p>`;
    html += `<p><strong>Total de Respondentes:</strong> ${reporte.totalRespondentes}</p>`;
    html += '</div>';

    html += '<h4 style="margin-bottom: 1rem;">Estadísticas por Pregunta</h4>';

    if (reporte.estadisticasPreguntas && reporte.estadisticasPreguntas.length > 0) {
        reporte.estadisticasPreguntas.forEach((estadistica, index) => {
            html += '<div class="card" style="margin-bottom: 1rem;">';
            html += `<h4>Pregunta ${index + 1}: ${estadistica.texto}</h4>`;
            html += `<p><strong>Tipo:</strong> ${estadistica.tipo}</p>`;
            html += `<p><strong>Total de Respuestas:</strong> ${estadistica.totalRespuestas}</p>`;

            if (estadistica.conteoOpciones) {
                html += '<h5>Distribución de Respuestas:</h5>';
                html += '<div class="table-container"><table class="table"><thead><tr><th>Opción</th><th>Cantidad</th><th>Porcentaje</th></tr></thead><tbody>';

                const total = estadistica.totalRespuestas;
                Object.entries(estadistica.conteoOpciones).forEach(([opcion, cantidad]) => {
                    const porcentaje = total > 0 ? ((cantidad / total) * 100).toFixed(2) : 0;
                    html += `<tr><td>${opcion}</td><td>${cantidad}</td><td>${porcentaje}%</td></tr>`;
                });

                html += '</tbody></table></div>';
            }

            html += '</div>';
        });
    } else {
        html += '<p>No hay estadísticas disponibles para esta encuesta.</p>';
    }

    html += '</div>';
    resultado.innerHTML = html;
}

function mostrarReporteEmpresa(reporte) {
    const resultado = document.getElementById('reporteResultado');

    let html = '<div class="card">';
    html += '<h3>Reporte por Empresa</h3>';
    html += `<p><strong>Total de Encuestas:</strong> ${reporte.totalEncuestas}</p>`;

    if (reporte.encuestas && reporte.encuestas.length > 0) {
        html += '<div class="table-container" style="margin-top: 1rem;">';
        html += '<table class="table"><thead><tr>';
        html += '<th>#</th>';
        html += '<th>Título</th>';
        html += '<th>Estado</th>';
        html += '<th>Total Respondentes</th>';
        html += '</tr></thead><tbody>';

        reporte.encuestas.forEach((encuesta, index) => {
            html += `<tr>
                <td>${index + 1}</td>
                <td>${encuesta.titulo}</td>
                <td><span class="badge badge-${encuesta.estado === 'ACTIVA' ? 'success' : encuesta.estado === 'FINALIZADA' ? 'warning' : 'info'}">${encuesta.estado}</span></td>
                <td>${encuesta.totalRespondentes}</td>
            </tr>`;
        });

        html += '</tbody></table></div>';
    } else {
        html += '<p>No hay encuestas para esta empresa.</p>';
    }

    html += '</div>';
    resultado.innerHTML = html;
}

