let empresas = [];

// Cargar empresas al iniciar
document.addEventListener('DOMContentLoaded', () => {
    cargarEmpresas();
    ui.closeModalOnClick('modalEmpresa');
});

async function cargarEmpresas() {
    const listContainer = document.getElementById('empresasList');
    ui.showLoading(listContainer);

    try {
        empresas = await api.get('/empresas');
        mostrarEmpresas();
    } catch (error) {
        listContainer.innerHTML = '<div class="alert alert-error">Error al cargar empresas: ' + error.message + '</div>';
    }
}

function mostrarEmpresas() {
    const listContainer = document.getElementById('empresasList');

    if (empresas.length === 0) {
        listContainer.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">🏢</div>
                <h3>No hay empresas registradas</h3>
                <p>Crea tu primera empresa para comenzar</p>
            </div>
        `;
        return;
    }

    let html = '<div class="table-container"><table class="table"><thead><tr>';
    html += '<th>ID</th>';
    html += '<th>Nombre</th>';
    html += '<th>NIT</th>';
    html += '<th>Email</th>';
    html += '<th>Teléfono</th>';
    html += '<th>Estado</th>';
    html += '<th>Acciones</th>';
    html += '</tr></thead><tbody>';

    empresas.forEach(empresa => {
        const estadoBadge = empresa.activa 
            ? '<span class="badge badge-success">Activa</span>'
            : '<span class="badge badge-danger">Inactiva</span>';

        html += `<tr>
            <td>${empresa.id}</td>
            <td>${empresa.nombre}</td>
            <td>${empresa.nit || '-'}</td>
            <td>${empresa.email || '-'}</td>
            <td>${empresa.telefono || '-'}</td>
            <td>${estadoBadge}</td>
            <td>
                <div class="actions">
                    <button class="btn btn-sm btn-primary" onclick="editarEmpresa(${empresa.id})">Editar</button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarEmpresa(${empresa.id})">Eliminar</button>
                </div>
            </td>
        </tr>`;
    });

    html += '</tbody></table></div>';
    listContainer.innerHTML = html;
}

function abrirModalCrear() {
    document.getElementById('modalEmpresaTitle').textContent = 'Nueva Empresa';
    document.getElementById('empresaId').value = '';
    ui.clearForm('formEmpresa');
    document.getElementById('activa').value = 'true';
    ui.showModal('modalEmpresa');
}

function editarEmpresa(id) {
    const empresa = empresas.find(e => e.id === id);
    if (!empresa) return;

    document.getElementById('modalEmpresaTitle').textContent = 'Editar Empresa';
    document.getElementById('empresaId').value = empresa.id;
    document.getElementById('nombre').value = empresa.nombre || '';
    document.getElementById('nit').value = empresa.nit || '';
    document.getElementById('direccion').value = empresa.direccion || '';
    document.getElementById('telefono').value = empresa.telefono || '';
    document.getElementById('email').value = empresa.email || '';
    document.getElementById('activa').value = empresa.activa ? 'true' : 'false';
    
    ui.showModal('modalEmpresa');
}

function cerrarModalEmpresa() {
    ui.hideModal('modalEmpresa');
    ui.clearForm('formEmpresa');
}

async function guardarEmpresa(event) {
    event.preventDefault();

    const empresaId = document.getElementById('empresaId').value;
    const empresaData = {
        nombre: document.getElementById('nombre').value,
        nit: document.getElementById('nit').value,
        direccion: document.getElementById('direccion').value,
        telefono: document.getElementById('telefono').value,
        email: document.getElementById('email').value,
        activa: document.getElementById('activa').value === 'true'
    };

    try {
        if (empresaId) {
            await api.put(`/empresas/${empresaId}`, empresaData);
            ui.showAlert('Empresa actualizada exitosamente', 'success');
        } else {
            await api.post('/empresas', empresaData);
            ui.showAlert('Empresa creada exitosamente', 'success');
        }
        
        cerrarModalEmpresa();
        cargarEmpresas();
    } catch (error) {
        ui.showAlert('Error al guardar empresa: ' + error.message, 'error');
    }
}

async function eliminarEmpresa(id) {
    if (!confirm('¿Está seguro de eliminar esta empresa? Esta acción no se puede deshacer.')) {
        return;
    }

    try {
        await api.delete(`/empresas/${id}`);
        ui.showAlert('Empresa eliminada exitosamente', 'success');
        cargarEmpresas();
    } catch (error) {
        ui.showAlert('Error al eliminar empresa: ' + error.message, 'error');
    }
}

