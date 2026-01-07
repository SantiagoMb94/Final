let encuestas = [];
let empresas = [];
let preguntas = [];
let opciones = [];
let encuestaActualId = null;

// Cargar datos al iniciar
document.addEventListener('DOMContentLoaded', () => {
    cargarEmpresas();
    cargarEncuestas();
    ui.closeModalOnClick('modalEncuesta');
    ui.closeModalOnClick('modalPreguntas');
    ui.closeModalOnClick('modalPregunta');
});

async function cargarEmpresas() {
    try {
        empresas = await api.get('/empresas?activas=true');
        const selectEmpresa = document.getElementById('empresaIdEncuesta');
        const filtroEmpresa = document.getElementById('filtroEmpresaEncuesta');

        empresas.forEach(empresa => {
            const option = document.createElement('option');
            option.value = empresa.id;
            option.textContent = empresa.nombre;
            selectEmpresa.appendChild(option.cloneNode(true));
            filtroEmpresa.appendChild(option.cloneNode(true));
        });
    } catch (error) {
        console.error('Error al cargar empresas:', error);
    }
}

async function cargarEncuestas() {
    const listContainer = document.getElementById('encuestasList');
    const empresaId = document.getElementById('filtroEmpresaEncuesta').value;
    const estado = document.getElementById('filtroEstado').value;
    ui.showLoading(listContainer);

    try {
        let url = '/encuestas';
        const params = [];
        if (empresaId) params.push(`empresaId=${empresaId}`);
        if (estado) params.push(`estado=${estado}`);
        if (params.length > 0) url += '?' + params.join('&');

        encuestas = await api.get(url);
        mostrarEncuestas();
    } catch (error) {
        listContainer.innerHTML = '<div class="alert alert-error">Error al cargar encuestas: ' + error.message + '</div>';
    }
}

function mostrarEncuestas() {
    const listContainer = document.getElementById('encuestasList');

    if (encuestas.length === 0) {
        listContainer.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">📝</div>
                <h3>No hay encuestas registradas</h3>
                <p>Crea tu primera encuesta para comenzar</p>
            </div>
        `;
        return;
    }

    let html = '<div class="table-container"><table class="table"><thead><tr>';
    html += '<th>#</th>';
    html += '<th>Título</th>';
    html += '<th>Empresa</th>';
    html += '<th>Estado</th>';
    html += '<th>Fecha Inicio</th>';
    html += '<th>Fecha Fin</th>';
    html += '<th>Acciones</th>';
    html += '</tr></thead><tbody>';

    encuestas.forEach((encuesta, index) => {
        const estadoBadge = encuesta.estado === 'ACTIVA'
            ? '<span class="badge badge-success">Activa</span>'
            : encuesta.estado === 'FINALIZADA'
                ? '<span class="badge badge-warning">Finalizada</span>'
                : '<span class="badge badge-info">Borrador</span>';

        html += `<tr>
            <td>${index + 1}</td>
            <td>${encuesta.titulo}</td>
            <td>${obtenerNombreEmpresa(encuesta.empresaId)}</td>
            <td>${estadoBadge}</td>
            <td>${ui.formatDate(encuesta.fechaInicio)}</td>
            <td>${ui.formatDate(encuesta.fechaFin)}</td>
            <td>
                <div class="actions">
                    <button class="btn btn-sm btn-primary" onclick="editarEncuesta('${encuesta.id}')">Editar</button>
                    <button class="btn btn-sm btn-success" onclick="gestionarPreguntas('${encuesta.id}')">Preguntas</button>
                    ${encuesta.estado === 'BORRADOR' ? `<button class="btn btn-sm btn-success" onclick="activarEncuesta('${encuesta.id}')">Activar</button>` : ''}
                    ${encuesta.estado === 'ACTIVA' ? `<button class="btn btn-sm btn-warning" onclick="finalizarEncuesta('${encuesta.id}')">Finalizar</button>` : ''}
                    <button class="btn btn-sm btn-danger" onclick="eliminarEncuesta('${encuesta.id}')">Eliminar</button>
                </div>
            </td>
        </tr>`;
    });

    html += '</tbody></table></div>';
    listContainer.innerHTML = html;
}

function obtenerNombreEmpresa(empresaId) {
    const empresa = empresas.find(e => e.id === empresaId);
    return empresa ? empresa.nombre : '-';
}

function abrirModalCrear() {
    document.getElementById('modalEncuestaTitle').textContent = 'Nueva Encuesta';
    document.getElementById('encuestaId').value = '';
    ui.clearForm('formEncuesta');
    document.getElementById('estado').value = 'BORRADOR';
    ui.showModal('modalEncuesta');
}

function editarEncuesta(id) {
    const encuesta = encuestas.find(e => e.id === id);
    if (!encuesta) return;

    document.getElementById('modalEncuestaTitle').textContent = 'Editar Encuesta';
    document.getElementById('encuestaId').value = encuesta.id;
    document.getElementById('titulo').value = encuesta.titulo || '';
    document.getElementById('descripcion').value = encuesta.descripcion || '';
    document.getElementById('estado').value = encuesta.estado || 'BORRADOR';
    document.getElementById('empresaIdEncuesta').value = encuesta.empresaId || '';

    if (encuesta.fechaInicio) {
        const fechaInicio = new Date(encuesta.fechaInicio);
        document.getElementById('fechaInicio').value = fechaInicio.toISOString().slice(0, 16);
    }
    if (encuesta.fechaFin) {
        const fechaFin = new Date(encuesta.fechaFin);
        document.getElementById('fechaFin').value = fechaFin.toISOString().slice(0, 16);
    }

    ui.showModal('modalEncuesta');
}

function cerrarModalEncuesta() {
    ui.hideModal('modalEncuesta');
    ui.clearForm('formEncuesta');
}

async function guardarEncuesta(event) {
    event.preventDefault();

    const encuestaId = document.getElementById('encuestaId').value;
    const fechaInicio = document.getElementById('fechaInicio').value;
    const fechaFin = document.getElementById('fechaFin').value;

    const encuestaData = {
        titulo: document.getElementById('titulo').value,
        descripcion: document.getElementById('descripcion').value,
        estado: document.getElementById('estado').value,
        empresaId: document.getElementById('empresaIdEncuesta').value,
        fechaInicio: fechaInicio ? new Date(fechaInicio).toISOString() : null,
        fechaFin: fechaFin ? new Date(fechaFin).toISOString() : null
    };

    try {
        if (encuestaId) {
            await api.put(`/encuestas/${encuestaId}`, encuestaData);
            ui.showAlert('Encuesta actualizada exitosamente', 'success');
        } else {
            await api.post('/encuestas', encuestaData);
            ui.showAlert('Encuesta creada exitosamente', 'success');
        }

        cerrarModalEncuesta();
        cargarEncuestas();
    } catch (error) {
        ui.showAlert('Error al guardar encuesta: ' + error.message, 'error');
    }
}

async function activarEncuesta(id) {
    try {
        await api.put(`/encuestas/${id}/activar`);
        ui.showAlert('Encuesta activada exitosamente', 'success');
        cargarEncuestas();
    } catch (error) {
        ui.showAlert('Error al activar encuesta: ' + error.message, 'error');
    }
}

async function finalizarEncuesta(id) {
    try {
        await api.put(`/encuestas/${id}/finalizar`);
        ui.showAlert('Encuesta finalizada exitosamente', 'success');
        cargarEncuestas();
    } catch (error) {
        ui.showAlert('Error al finalizar encuesta: ' + error.message, 'error');
    }
}

async function eliminarEncuesta(id) {
    if (!confirm('¿Está seguro de eliminar esta encuesta? Esta acción eliminará todas las preguntas y respuestas asociadas.')) {
        return;
    }

    try {
        await api.delete(`/encuestas/${id}`);
        ui.showAlert('Encuesta eliminada exitosamente', 'success');
        cargarEncuestas();
    } catch (error) {
        ui.showAlert('Error al eliminar encuesta: ' + error.message, 'error');
    }
}

// Gestión de Preguntas
async function gestionarPreguntas(encuestaId) {
    encuestaActualId = encuestaId;
    const encuesta = encuestas.find(e => e.id === encuestaId);
    document.getElementById('encuestaTituloPreguntas').textContent = encuesta ? encuesta.titulo : '';

    await cargarPreguntas(encuestaId);
    ui.showModal('modalPreguntas');
}

async function cargarPreguntas(encuestaId) {
    const listContainer = document.getElementById('preguntasList');
    ui.showLoading(listContainer);

    try {
        preguntas = await api.get(`/preguntas?encuestaId=${encuestaId}`);
        mostrarPreguntas();
    } catch (error) {
        listContainer.innerHTML = '<div class="alert alert-error">Error al cargar preguntas: ' + error.message + '</div>';
    }
}

function mostrarPreguntas() {
    const listContainer = document.getElementById('preguntasList');

    if (preguntas.length === 0) {
        listContainer.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">❓</div>
                <h3>No hay preguntas en esta encuesta</h3>
                <p>Agrega preguntas para comenzar</p>
            </div>
        `;
        return;
    }

    let html = '<div class="table-container"><table class="table"><thead><tr>';
    html += '<th>Orden</th>';
    html += '<th>Texto</th>';
    html += '<th>Tipo</th>';
    html += '<th>Obligatoria</th>';
    html += '<th>Acciones</th>';
    html += '</tr></thead><tbody>';

    preguntas.forEach(pregunta => {
        const obligatoriaBadge = pregunta.obligatoria
            ? '<span class="badge badge-warning">Sí</span>'
            : '<span class="badge badge-info">No</span>';

        html += `<tr>
            <td>${pregunta.orden || '-'}</td>
            <td>${pregunta.texto}</td>
            <td>${pregunta.tipo}</td>
            <td>${obligatoriaBadge}</td>
            <td>
                <div class="actions">
                    <button class="btn btn-sm btn-primary" onclick="editarPregunta(${pregunta.id})">Editar</button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarPregunta(${pregunta.id})">Eliminar</button>
                </div>
            </td>
        </tr>`;
    });

    html += '</tbody></table></div>';
    listContainer.innerHTML = html;
}

function cerrarModalPreguntas() {
    ui.hideModal('modalPreguntas');
    encuestaActualId = null;
}

function abrirModalCrearPregunta() {
    document.getElementById('modalPreguntaTitle').textContent = 'Nueva Pregunta';
    document.getElementById('preguntaId').value = '';
    document.getElementById('encuestaIdPregunta').value = encuestaActualId;
    ui.clearForm('formPregunta');
    document.getElementById('obligatoriaPregunta').checked = false;
    document.getElementById('tipoPregunta').value = 'OPCION_MULTIPLE';
    toggleOpciones();
    ui.showModal('modalPregunta');
}

function toggleOpciones() {
    const tipo = document.getElementById('tipoPregunta').value;
    const opcionesContainer = document.getElementById('opcionesContainer');
    if (tipo === 'OPCION_MULTIPLE' || tipo === 'ESCALA') {
        opcionesContainer.style.display = 'block';
        cargarOpcionesPregunta();
    } else {
        opcionesContainer.style.display = 'none';
    }
}

let opcionesPregunta = [];

function cargarOpcionesPregunta() {
    const preguntaId = document.getElementById('preguntaId').value;
    if (preguntaId) {
        // Cargar opciones existentes
        api.get(`/opciones?preguntaId=${preguntaId}`).then(opciones => {
            opcionesPregunta = opciones;
            mostrarOpciones();
        });
    } else {
        opcionesPregunta = [];
        mostrarOpciones();
    }
}

function mostrarOpciones() {
    const opcionesList = document.getElementById('opcionesList');
    if (opcionesPregunta.length === 0) {
        opcionesList.innerHTML = '<p style="color: var(--text-secondary);">No hay opciones. Agrega al menos una opción.</p>';
        return;
    }

    let html = '';
    opcionesPregunta.forEach((opcion, index) => {
        html += `
            <div style="display: flex; gap: 0.5rem; margin-bottom: 0.5rem; align-items: center;">
                <input type="text" class="form-control" value="${opcion.texto || ''}" 
                       onchange="opcionesPregunta[${index}].texto = this.value" 
                       placeholder="Texto de la opción">
                <input type="number" class="form-control" style="width: 100px;" value="${opcion.orden || index + 1}" 
                       onchange="opcionesPregunta[${index}].orden = parseInt(this.value)" 
                       placeholder="Orden">
                <button type="button" class="btn btn-sm btn-danger" onclick="eliminarOpcion(${index})">Eliminar</button>
            </div>
        `;
    });
    opcionesList.innerHTML = html;
}

function agregarOpcion() {
    opcionesPregunta.push({ texto: '', orden: opcionesPregunta.length + 1 });
    mostrarOpciones();
}

function eliminarOpcion(index) {
    opcionesPregunta.splice(index, 1);
    mostrarOpciones();
}

async function editarPregunta(id) {
    const pregunta = preguntas.find(p => p.id === id);
    if (!pregunta) return;

    document.getElementById('modalPreguntaTitle').textContent = 'Editar Pregunta';
    document.getElementById('preguntaId').value = pregunta.id;
    document.getElementById('encuestaIdPregunta').value = pregunta.encuestaId;
    document.getElementById('textoPregunta').value = pregunta.texto || '';
    document.getElementById('tipoPregunta').value = pregunta.tipo || 'OPCION_MULTIPLE';
    document.getElementById('obligatoriaPregunta').checked = pregunta.obligatoria || false;
    document.getElementById('ordenPregunta').value = pregunta.orden || '';

    toggleOpciones();
    await cargarOpcionesPregunta();
    ui.showModal('modalPregunta');
}

function cerrarModalPregunta() {
    ui.hideModal('modalPregunta');
    ui.clearForm('formPregunta');
    opcionesPregunta = [];
}

async function guardarPregunta(event) {
    event.preventDefault();

    const preguntaId = document.getElementById('preguntaId').value;
    const encuestaId = document.getElementById('encuestaIdPregunta').value;

    const preguntaData = {
        texto: document.getElementById('textoPregunta').value,
        tipo: document.getElementById('tipoPregunta').value,
        obligatoria: document.getElementById('obligatoriaPregunta').checked,
        orden: parseInt(document.getElementById('ordenPregunta').value) || null,
        encuestaId: encuestaId
    };

    try {
        let preguntaGuardada;
        if (preguntaId) {
            preguntaGuardada = await api.put(`/preguntas/${preguntaId}`, preguntaData);
        } else {
            preguntaGuardada = await api.post('/preguntas', preguntaData);
        }

        // Guardar opciones si es necesario
        const tipo = preguntaData.tipo;
        if ((tipo === 'OPCION_MULTIPLE' || tipo === 'ESCALA') && opcionesPregunta.length > 0) {
            // Eliminar opciones existentes si estamos editando
            if (preguntaId) {
                const opcionesExistentes = await api.get(`/opciones?preguntaId=${preguntaId}`);
                for (const opcion of opcionesExistentes) {
                    await api.delete(`/opciones/${opcion.id}`);
                }
            }

            // Crear nuevas opciones
            for (const opcion of opcionesPregunta) {
                if (opcion.texto.trim()) {
                    await api.post('/opciones', {
                        texto: opcion.texto,
                        orden: opcion.orden,
                        preguntaId: preguntaGuardada.id
                    });
                }
            }
        }

        ui.showAlert('Pregunta guardada exitosamente', 'success');
        cerrarModalPregunta();
        await cargarPreguntas(encuestaId);
    } catch (error) {
        ui.showAlert('Error al guardar pregunta: ' + error.message, 'error');
    }
}

async function eliminarPregunta(id) {
    if (!confirm('¿Está seguro de eliminar esta pregunta? Esta acción eliminará todas las opciones y respuestas asociadas.')) {
        return;
    }

    try {
        await api.delete(`/preguntas/${id}`);
        ui.showAlert('Pregunta eliminada exitosamente', 'success');
        await cargarPreguntas(encuestaActualId);
    } catch (error) {
        ui.showAlert('Error al eliminar pregunta: ' + error.message, 'error');
    }
}

