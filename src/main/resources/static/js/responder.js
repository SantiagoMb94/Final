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

        // Limpiar opciones existentes (excepto la primera)
        while (selectEncuesta.children.length > 1) {
            selectEncuesta.removeChild(selectEncuesta.lastChild);
        }

        if (encuestas.length === 0) {
            const option = document.createElement('option');
            option.value = '';
            option.textContent = 'No hay encuestas activas disponibles';
            option.disabled = true;
            selectEncuesta.appendChild(option);
            ui.showAlert('No hay encuestas activas. Activa una encuesta desde la sección de Encuestas.', 'info');
        } else {
            encuestas.forEach(encuesta => {
                const option = document.createElement('option');
                option.value = encuesta.id;
                option.textContent = encuesta.titulo;
                selectEncuesta.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error al cargar encuestas:', error);
        ui.showAlert('Error al cargar encuestas activas: ' + error.message, 'error');
    }
}

async function cargarEncuesta() {
    const encuestaId = document.getElementById('selectEncuestaResponder').value;
    console.log('Cargando encuesta con ID:', encuestaId);

    if (!encuestaId || encuestaId === '') {
        document.getElementById('encuestaContainer').style.display = 'none';
        return;
    }

    const container = document.getElementById('encuestaContainer');
    container.style.display = 'block';

    // Guardar el HTML original antes de mostrar loading
    if (!container.dataset.originalHTML) {
        container.dataset.originalHTML = container.innerHTML;
    }

    // Mostrar loading sin destruir la estructura
    const loadingDiv = document.createElement('div');
    loadingDiv.id = 'loadingOverlay';
    loadingDiv.style.cssText = 'text-align: center; padding: 2rem;';
    loadingDiv.innerHTML = '<div class="loading"><div class="spinner"></div><p>Cargando encuesta...</p></div>';

    // Ocultar el contenido original temporalmente
    const originalContent = container.querySelector('#encuestaTitulo')?.parentElement;
    if (originalContent) {
        originalContent.style.display = 'none';
    }
    const form = container.querySelector('#formRespuestas');
    if (form) {
        form.style.display = 'none';
    }

    // Agregar loading al inicio
    container.insertBefore(loadingDiv, container.firstChild);

    try {
        // Buscar la encuesta en el array cargado o cargarla directamente
        encuestaActual = encuestas.find(e => e.id == encuestaId);
        if (!encuestaActual) {
            encuestaActual = await api.get(`/encuestas/${encuestaId}`);
        }

        // Cargar preguntas
        preguntasActuales = await api.get(`/preguntas?encuestaId=${encuestaId}`);

        if (preguntasActuales.length === 0) {
            // Remover loading
            const loadingOverlay = document.getElementById('loadingOverlay');
            if (loadingOverlay) {
                loadingOverlay.remove();
            }
            container.innerHTML = '<div class="alert alert-warning">Esta encuesta no tiene preguntas. Agrega preguntas desde la sección de Encuestas antes de responder.</div>';
            container.style.display = 'block';
            return;
        }

        // Cargar opciones para cada pregunta
        opcionesPorPregunta = {};
        let preguntasSinOpciones = [];

        for (const pregunta of preguntasActuales) {
            if (pregunta.tipo === 'OPCION_MULTIPLE' || pregunta.tipo === 'ESCALA') {
                try {
                    const opciones = await api.get(`/opciones?preguntaId=${pregunta.id}`);
                    opcionesPorPregunta[pregunta.id] = opciones || [];

                    // Verificar si hay opciones
                    if (!opciones || opciones.length === 0) {
                        preguntasSinOpciones.push(pregunta.texto);
                        console.warn(`La pregunta "${pregunta.texto}" (ID: ${pregunta.id}) no tiene opciones configuradas`);
                    }
                } catch (error) {
                    console.error(`Error al cargar opciones para pregunta ${pregunta.id}:`, error);
                    opcionesPorPregunta[pregunta.id] = [];
                    preguntasSinOpciones.push(pregunta.texto);
                }
            }
        }

        // Mostrar advertencia si hay preguntas sin opciones
        if (preguntasSinOpciones.length > 0) {
            const mensaje = `Las siguientes preguntas no tienen opciones configuradas: ${preguntasSinOpciones.join(', ')}. Por favor, agrega opciones desde la sección de Encuestas.`;
            console.warn(mensaje);
        }

        mostrarEncuesta();
    } catch (error) {
        console.error('Error completo al cargar encuesta:', error);

        // Remover loading
        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.remove();
        }

        container.innerHTML = '<div class="alert alert-error">Error al cargar la encuesta: ' + (error.message || 'Error desconocido') + '</div>';
        container.style.display = 'block';
    }
}

function mostrarEncuesta() {
    const container = document.getElementById('encuestaContainer');

    // Remover loading overlay
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
    }

    if (!encuestaActual) {
        container.innerHTML = '<div class="alert alert-error">No se pudo cargar la información de la encuesta.</div>';
        container.style.display = 'block';
        return;
    }

    // Restaurar la estructura si fue destruida
    const encuestaTitulo = document.getElementById('encuestaTitulo');
    const encuestaDescripcion = document.getElementById('encuestaDescripcion');

    if (!encuestaTitulo || !encuestaDescripcion) {
        // Restaurar HTML original
        if (container.dataset.originalHTML) {
            container.innerHTML = container.dataset.originalHTML;
        } else {
            // Recrear estructura si no hay original guardado
            container.innerHTML = `
                <div class="card" style="margin-bottom: 2rem;">
                    <h3 id="encuestaTitulo"></h3>
                    <p id="encuestaDescripcion"></p>
                </div>
                <form id="formRespuestas" onsubmit="enviarRespuestas(event)">
                    <div id="preguntasContainer"></div>
                    <div class="form-group">
                        <label class="form-label" for="identificadorRespondente">Identificador (Email o ID)</label>
                        <input type="text" class="form-control" id="identificadorRespondente" placeholder="Ingrese su email o identificador">
                    </div>
                    <div style="display: flex; gap: 1rem; justify-content: flex-end; margin-top: 2rem;">
                        <button type="button" class="btn btn-secondary" onclick="limpiarFormulario()">Limpiar</button>
                        <button type="submit" class="btn btn-primary">Enviar Respuestas</button>
                    </div>
                </form>
            `;
        }
    }

    // Ahora sí podemos establecer el contenido
    document.getElementById('encuestaTitulo').textContent = encuestaActual.titulo;
    document.getElementById('encuestaDescripcion').textContent = encuestaActual.descripcion || '';

    const preguntasContainer = document.getElementById('preguntasContainer');
    let html = '';

    console.log('Mostrando encuesta:', encuestaActual);
    console.log('Preguntas:', preguntasActuales);
    console.log('Opciones por pregunta:', opcionesPorPregunta);

    preguntasActuales.forEach((pregunta, index) => {
        html += '<div class="card" style="margin-bottom: 1.5rem;">';
        html += `<h4>${index + 1}. ${pregunta.texto} ${pregunta.obligatoria ? '<span style="color: red;">*</span>' : ''}</h4>`;

        const preguntaId = `pregunta_${pregunta.id}`;

        switch (pregunta.tipo) {
            case 'OPCION_MULTIPLE':
            case 'ESCALA':
                const opciones = opcionesPorPregunta[pregunta.id] || [];
                if (opciones.length === 0) {
                    html += '<div class="alert alert-warning" style="margin-top: 0.5rem;">Esta pregunta no tiene opciones configuradas. Por favor, agrega opciones desde la sección de Encuestas.</div>';
                } else {
                    opciones.forEach(opcion => {
                        html += `
                            <div style="margin: 0.5rem 0;">
                                <label style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer;">
                                    <input type="radio" name="${preguntaId}" value="${opcion.id}" 
                                           ${pregunta.obligatoria ? 'required' : ''}>
                                    <span>${opcion.texto || 'Opción sin texto'}</span>
                                </label>
                            </div>
                        `;
                    });
                }
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

    // Mostrar el formulario
    const form = document.getElementById('formRespuestas');
    if (form) {
        form.style.display = 'block';
    }

    // Mostrar el título y descripción
    const titleCard = document.getElementById('encuestaTitulo')?.parentElement;
    if (titleCard) {
        titleCard.style.display = 'block';
    }

    container.style.display = 'block';

    // Scroll suave al contenedor de la encuesta
    container.scrollIntoView({ behavior: 'smooth', block: 'start' });
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
                respuestaData.opcionId = respuestaElement.value;
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

