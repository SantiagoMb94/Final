package co.edu.poli.encuestas.negocio.servicio;

import co.edu.poli.encuestas.datos.repositorio.EmpresaRepositorio;
import co.edu.poli.encuestas.datos.repositorio.UsuarioRepositorio;
import co.edu.poli.encuestas.modelo.Empresa;
import co.edu.poli.encuestas.modelo.Usuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServicio {
    
    private final UsuarioRepositorio usuarioRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }
    
    public List<Usuario> listarPorEmpresa(Long empresaId) {
        return usuarioRepositorio.findByEmpresaId(empresaId);
    }
    
    public List<Usuario> listarActivosPorEmpresa(Long empresaId) {
        return usuarioRepositorio.findByEmpresaIdAndActivoTrue(empresaId);
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email);
    }
    
    @Transactional
    public Usuario crear(Usuario usuario, Long empresaId) {
        Empresa empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + empresaId));
        
        if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        
        usuario.setEmpresa(empresa);
        return usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        return usuarioRepositorio.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setApellido(usuarioActualizado.getApellido());
                    usuario.setEmail(usuarioActualizado.getEmail());
                    if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {
                        usuario.setContrasena(usuarioActualizado.getContrasena());
                    }
                    usuario.setRol(usuarioActualizado.getRol());
                    usuario.setActivo(usuarioActualizado.getActivo());
                    return usuarioRepositorio.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }
    
    @Transactional
    public void eliminar(Long id) {
        usuarioRepositorio.deleteById(id);
    }
    
    @Transactional
    public void desactivar(Long id) {
        usuarioRepositorio.findById(id)
                .ifPresent(usuario -> {
                    usuario.setActivo(false);
                    usuarioRepositorio.save(usuario);
                });
    }
}

