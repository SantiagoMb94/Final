let encuestas = [];
let encuestaActual = null;
let preguntasActuales = [];
let opcionesPorPregunta = {};

document.addEventListener('DOMContentLoaded', () => {
    cargarEncuestasActivas();
});

async function cargarEncuestasActivas() {
    try {
        encuestas = await api.get('/encuestas?estado=ACTIVA');
        const selectEncuesta = document.getElementById('selectEncuestaResponder');
        
        encuestas.forEach(encuesta => {
            const option = document.createElement('option');
            option.value = encuesta.id;
            option.textContent = encuesta.titulo;
            selectEncuesta.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar encuestas:', error);
        ui.showAlert('Error al cargar encuestas activas', 'error');
    }
}

async function cargarEncuesta() {
    const encuestaId = document.getElementById('selectEncuestaResponder').value;
    if (!encuestaId) {
        document.getElementById('encuestaContainer').style.display = 'none';
        return;
    }

    const container = document.getElementById('encuestaContainer');
    ui.showLoading(container);

    try {
        encuestaActual = encuestas.find(e => e.id == encuestaId);
        if (!encuestaActual) {
            encuestaActual = await api.get(`/encuestas/${encuestaId}`);
        }

        preguntasActuales = await api.get(`/preguntas?encuestaId=${encuestaId}`);
        
        // Cargar opciones para cada pregunta
        for (const pregunta of preguntasActuales) {
            if (pregunta.tipo === 'OPCION_MULTIPLE' || pregunta.tipo === 'ESCALA') {
                opcionesPorPregunta[pregunta.id] = await api.get(`/opciones?preguntaId=${pregunta.id}`);
            }
        }

        mostrarEncuesta();
    } catch (error) {
        container.innerHTML = '<div class="alert alert-error">Error al cargar la encuesta: ' + error.message + '</div>';
    }
}

function mostrarEncuesta() {
    document.getElementById('encuestaTitulo').textContent = encuestaActual.titulo;
    document.getElementById('encuestaDescripcion').textContent = encuestaActual.descripcion || '';

    const preguntasContainer = document.getElementById('preguntasContainer');
    let html = '';

    preguntasActuales.forEach((pregunta, index) => {
        html += '<div class="card" style="margin-bottom: 1.5rem;">';
        html += `<h4>${index + 1}. ${pregunta.texto} ${pregunta.obligatoria ? '<span style="color: red;">*</span>' : ''}</h4>`;
        
        const preguntaId = `pregunta_${pregunta.id}`;
        
        switch (pregunta.tipo) {
            case 'OPCION_MULTIPLE':
            case 'ESCALA':
                const opciones = opcionesPorPregunta[pregunta.id] || [];
                opciones.forEach(opcion => {
                    html += `
                        <div style="margin: 0.5rem 0;">
                            <label style="display: flex; align-items: center; gap: 0.5rem;">
                                <input type="radio" name="${preguntaId}" value="${opcion.id}" 
                                       ${pregunta.obligatoria ? 'required' : ''}>
                                <span>${opcion.texto}</span>
                            </label>
                        </div>
                    `;
                });
                break;
                
            case 'SI_NO':
                html += `
                    <div style="margin: 0.5rem 0;">
                        <label style="display: flex; align-items: center; gap: 0.5rem;">
                            <input type="radio" name="${preguntaId}" value="SI" ${pregunta.obligatoria ? 'required' : ''}>
                            <span>Sí</span>
                        </label>
                    </div>
                    <div style="margin: 0.5rem 0;">
                        <label style="display: flex; align-items: center; gap: 0.5rem;">
                            <input type="radio" name="${preguntaId}" value="NO" ${pregunta.obligatoria ? 'required' : ''}>
                            <span>No</span>
                        </label>
                    </div>
                `;
                break;
                
            case 'TEXTO_LIBRE':
                html += `
                    <textarea class="form-control" name="${preguntaId}" rows="4" 
                              placeholder="Escriba su respuesta aquí" 
                              ${pregunta.obligatoria ? 'required' : ''}></textarea>
                `;
                break;
        }
        
        html += '</div>';
    });

    preguntasContainer.innerHTML = html;
    document.getElementById('encuestaContainer').style.display = 'block';
}

async function enviarRespuestas(event) {
    event.preventDefault();

    const identificadorRespondente = document.getElementById('identificadorRespondente').value;
    
    if (!identificadorRespondente) {
        ui.showAlert('Por favor ingrese un identificador (email o ID)', 'error');
        return;
    }

    try {
        // Enviar respuesta para cada pregunta
        for (const pregunta of preguntasActuales) {
            const preguntaId = `pregunta_${pregunta.id}`;
            const respuestaElement = document.querySelector(`[name="${preguntaId}"]:checked`) || 
                                   document.querySelector(`[name="${preguntaId}"]`);
            
            if (!respuestaElement) continue;

            let respuestaData = {
                encuestaId: encuestaActual.id,
                preguntaId: pregunta.id,
                identificadorRespondente: identificadorRespondente
            };

            if (pregunta.tipo === 'OPCION_MULTIPLE' || pregunta.tipo === 'ESCALA') {
                respuestaData.opcionId = parseInt(respuestaElement.value);
            } else if (pregunta.tipo === 'TEXTO_LIBRE') {
                respuestaData.valor = respuestaElement.value;
            } else if (pregunta.tipo === 'SI_NO') {
                respuestaData.valor = respuestaElement.value;
            }

            await api.post('/respuestas', respuestaData);
        }

        ui.showAlert('¡Respuestas enviadas exitosamente!', 'success');
        limpiarFormulario();
        document.getElementById('selectEncuestaResponder').value = '';
        document.getElementById('encuestaContainer').style.display = 'none';
    } catch (error) {
        ui.showAlert('Error al enviar respuestas: ' + error.message, 'error');
    }
}

function limpiarFormulario() {
    document.getElementById('formRespuestas').reset();
}

