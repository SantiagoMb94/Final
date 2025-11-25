package co.edu.poli.encuestas.presentacion.controlador;

import co.edu.poli.encuestas.modelo.Usuario;
import co.edu.poli.encuestas.negocio.servicio.UsuarioServicio;
import co.edu.poli.encuestas.presentacion.dto.UsuarioDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioControlador {
    
    private final UsuarioServicio usuarioServicio;
    
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos(@RequestParam(required = false) Long empresaId) {
        List<Usuario> usuarios;
        if (empresaId != null) {
            usuarios = usuarioServicio.listarPorEmpresa(empresaId);
        } else {
            usuarios = usuarioServicio.listarTodos();
        }
        
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(usuariosDTO);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        return usuarioServicio.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(convertirADTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getEmpresaId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Usuario usuario = convertirAEntidad(usuarioDTO);
        Usuario usuarioCreado = usuarioServicio.crear(usuario, usuarioDTO.getEmpresaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(usuarioCreado));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = convertirAEntidad(usuarioDTO);
        Usuario usuarioActualizado = usuarioServicio.actualizar(id, usuario);
        return ResponseEntity.ok(convertirADTO(usuarioActualizado));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioServicio.desactivar(id);
        return ResponseEntity.ok().build();
    }
    
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setActivo(usuario.getActivo());
        dto.setEmpresaId(usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null);
        return dto;
    }
    
    private Usuario convertirAEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena());
        usuario.setRol(dto.getRol() != null ? dto.getRol() : "USUARIO");
        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }
        return usuario;
    }
}

