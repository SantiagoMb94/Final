package co.edu.poli.encuestas.presentacion.controlador;

import co.edu.poli.encuestas.modelo.Empresa;
import co.edu.poli.encuestas.negocio.servicio.EmpresaServicio;
import co.edu.poli.encuestas.presentacion.dto.EmpresaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaControlador {

    private final EmpresaServicio empresaServicio;

    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listarTodas(@RequestParam(required = false) Boolean activas) {
        List<Empresa> empresas;
        if (activas != null && activas) {
            empresas = empresaServicio.listarActivas();
        } else {
            empresas = empresaServicio.listarTodas();
        }

        List<EmpresaDTO> empresasDTO = empresas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(empresasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarPorId(@PathVariable java.util.UUID id) {
        return empresaServicio.buscarPorId(id)
                .map(empresa -> ResponseEntity.ok(convertirADTO(empresa)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaDTO> crear(@Valid @RequestBody EmpresaDTO empresaDTO) {
        Empresa empresa = convertirAEntidad(empresaDTO);
        Empresa empresaCreada = empresaServicio.crear(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(empresaCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> actualizar(@PathVariable java.util.UUID id,
            @Valid @RequestBody EmpresaDTO empresaDTO) {
        Empresa empresa = convertirAEntidad(empresaDTO);
        Empresa empresaActualizada = empresaServicio.actualizar(id, empresa);
        return ResponseEntity.ok(convertirADTO(empresaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable java.util.UUID id) {
        empresaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable java.util.UUID id) {
        empresaServicio.desactivar(id);
        return ResponseEntity.ok().build();
    }

    private EmpresaDTO convertirADTO(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setNit(empresa.getNit());
        dto.setDireccion(empresa.getDireccion());
        dto.setTelefono(empresa.getTelefono());
        dto.setEmail(empresa.getEmail());
        dto.setActiva(empresa.getActiva());
        return dto;
    }

    private Empresa convertirAEntidad(EmpresaDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setNit(dto.getNit());
        empresa.setDireccion(dto.getDireccion());
        empresa.setTelefono(dto.getTelefono());
        empresa.setEmail(dto.getEmail());
        if (dto.getActiva() != null) {
            empresa.setActiva(dto.getActiva());
        }
        return empresa;
    }
}
