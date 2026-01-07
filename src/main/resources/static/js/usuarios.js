let usuarios = [];
let empresas = [];

// Cargar datos al iniciar
document.addEventListener('DOMContentLoaded', () => {
    cargarEmpresas();
    cargarUsuarios();
    ui.closeModalOnClick('modalUsuario');
});

async function cargarEmpresas() {
    try {
        empresas = await api.get('/empresas?activas=true');
        const selectEmpresa = document.getElementById('empresaId');
        const filtroEmpresa = document.getElementById('filtroEmpresa');

        empresas.forEach(empresa => {
            const option = document.createElement('option');
            option.value = empresa.id;
            option.textContent = empresa.nombre;
            selectEmpresa.appendChild(option.cloneNode(true));
            filtroEmpresa.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar empresas:', error);
    }
}

async function cargarUsuarios() {
    const listContainer = document.getElementById('usuariosList');
    const empresaId = document.getElementById('filtroEmpresa').value;
    ui.showLoading(listContainer);

    try {
        const url = empresaId ? `/usuarios?empresaId=${empresaId}` : '/usuarios';
        usuarios = await api.get(url);
        mostrarUsuarios();
    } catch (error) {
        listContainer.innerHTML = '<div class="alert alert-error">Error al cargar usuarios: ' + error.message + '</div>';
    }
}

function mostrarUsuarios() {
    const listContainer = document.getElementById('usuariosList');

    if (usuarios.length === 0) {
        listContainer.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">👥</div>
                <h3>No hay usuarios registrados</h3>
                <p>Crea tu primer usuario para comenzar</p>
            </div>
        `;
        return;
    }

    let html = '<div class="table-container"><table class="table"><thead><tr>';
    html += '<th>#</th>';
    html += '<th>Nombre</th>';
    html += '<th>Email</th>';
    html += '<th>Rol</th>';
    html += '<th>Empresa</th>';
    html += '<th>Estado</th>';
    html += '<th>Acciones</th>';
    html += '</tr></thead><tbody>';

    usuarios.forEach((usuario, index) => {
        const estadoBadge = usuario.activo
            ? '<span class="badge badge-success">Activo</span>'
            : '<span class="badge badge-danger">Inactivo</span>';

        const rolBadge = usuario.rol === 'ADMINISTRADOR'
            ? '<span class="badge badge-info">Administrador</span>'
            : '<span class="badge badge-info">Usuario</span>';

        html += `<tr>
            <td>${index + 1}</td>
            <td>${usuario.nombre} ${usuario.apellido}</td>
            <td>${usuario.email}</td>
            <td>${rolBadge}</td>
            <td>${obtenerNombreEmpresa(usuario.empresaId)}</td>
            <td>${estadoBadge}</td>
            <td>
                <div class="actions">
                    <button class="btn btn-sm btn-primary" onclick="editarUsuario('${usuario.id}')">Editar</button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarUsuario('${usuario.id}')">Eliminar</button>
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
    document.getElementById('modalUsuarioTitle').textContent = 'Nuevo Usuario';
    document.getElementById('usuarioId').value = '';
    ui.clearForm('formUsuario');
    document.getElementById('activo').value = 'true';
    document.getElementById('rol').value = 'USUARIO';
    ui.showModal('modalUsuario');
}

function editarUsuario(id) {
    const usuario = usuarios.find(u => u.id === id);
    if (!usuario) return;

    document.getElementById('modalUsuarioTitle').textContent = 'Editar Usuario';
    document.getElementById('usuarioId').value = usuario.id;
    document.getElementById('nombre').value = usuario.nombre || '';
    document.getElementById('apellido').value = usuario.apellido || '';
    document.getElementById('email').value = usuario.email || '';
    document.getElementById('rol').value = usuario.rol || 'USUARIO';
    document.getElementById('empresaId').value = usuario.empresaId || '';
    document.getElementById('activo').value = usuario.activo ? 'true' : 'false';
    document.getElementById('contrasena').required = false;

    ui.showModal('modalUsuario');
}

function cerrarModalUsuario() {
    ui.hideModal('modalUsuario');
    ui.clearForm('formUsuario');
    document.getElementById('contrasena').required = true;
}

async function guardarUsuario(event) {
    event.preventDefault();

    const usuarioId = document.getElementById('usuarioId').value;
    const usuarioData = {
        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        email: document.getElementById('email').value,
        contrasena: document.getElementById('contrasena').value,
        rol: document.getElementById('rol').value,
        empresaId: document.getElementById('empresaId').value,
        activo: document.getElementById('activo').value === 'true'
    };

    try {
        if (usuarioId) {
            // Si no se cambió la contraseña, no enviarla
            if (!usuarioData.contrasena) {
                delete usuarioData.contrasena;
            }
            await api.put(`/usuarios/${usuarioId}`, usuarioData);
            ui.showAlert('Usuario actualizado exitosamente', 'success');
        } else {
            await api.post('/usuarios', usuarioData);
            ui.showAlert('Usuario creado exitosamente', 'success');
        }

        cerrarModalUsuario();
        cargarUsuarios();
    } catch (error) {
        ui.showAlert('Error al guardar usuario: ' + error.message, 'error');
    }
}

async function eliminarUsuario(id) {
    if (!confirm('¿Está seguro de eliminar este usuario? Esta acción no se puede deshacer.')) {
        return;
    }

    try {
        await api.delete(`/usuarios/${id}`);
        ui.showAlert('Usuario eliminado exitosamente', 'success');
        cargarUsuarios();
    } catch (error) {
        ui.showAlert('Error al eliminar usuario: ' + error.message, 'error');
    }
}

