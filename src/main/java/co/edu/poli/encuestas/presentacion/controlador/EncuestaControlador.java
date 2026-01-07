package co.edu.poli.encuestas.presentacion.controlador;

import co.edu.poli.encuestas.modelo.Encuesta;
import co.edu.poli.encuestas.negocio.servicio.EncuestaServicio;
import co.edu.poli.encuestas.presentacion.dto.EncuestaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encuestas")
@RequiredArgsConstructor
public class EncuestaControlador {

    private final EncuestaServicio encuestaServicio;

    @GetMapping
    public ResponseEntity<List<EncuestaDTO>> listarTodas(
            @RequestParam(required = false) java.util.UUID empresaId,
            @RequestParam(required = false) String estado) {
        List<Encuesta> encuestas;
        if (empresaId != null && estado != null) {
            encuestas = encuestaServicio.listarPorEmpresaYEstado(empresaId, estado);
        } else if (empresaId != null) {
            encuestas = encuestaServicio.listarPorEmpresa(empresaId);
        } else {
            encuestas = encuestaServicio.listarTodas();
        }

        List<EncuestaDTO> encuestasDTO = encuestas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(encuestasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncuestaDTO> buscarPorId(@PathVariable java.util.UUID id) {
        return encuestaServicio.buscarPorId(id)
                .map(encuesta -> ResponseEntity.ok(convertirADTO(encuesta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EncuestaDTO> crear(@Valid @RequestBody EncuestaDTO encuestaDTO) {
        if (encuestaDTO.getEmpresaId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Encuesta encuesta = convertirAEntidad(encuestaDTO);
        Encuesta encuestaCreada = encuestaServicio.crear(encuesta, encuestaDTO.getEmpresaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(encuestaCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EncuestaDTO> actualizar(@PathVariable java.util.UUID id,
            @Valid @RequestBody EncuestaDTO encuestaDTO) {
        Encuesta encuesta = convertirAEntidad(encuestaDTO);
        Encuesta encuestaActualizada = encuestaServicio.actualizar(id, encuesta);
        return ResponseEntity.ok(convertirADTO(encuestaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable java.util.UUID id) {
        encuestaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<EncuestaDTO> activar(@PathVariable java.util.UUID id) {
        Encuesta encuesta = encuestaServicio.activar(id);
        return ResponseEntity.ok(convertirADTO(encuesta));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<EncuestaDTO> finalizar(@PathVariable java.util.UUID id) {
        Encuesta encuesta = encuestaServicio.finalizar(id);
        return ResponseEntity.ok(convertirADTO(encuesta));
    }

    private EncuestaDTO convertirADTO(Encuesta encuesta) {
        EncuestaDTO dto = new EncuestaDTO();
        dto.setId(encuesta.getId());
        dto.setTitulo(encuesta.getTitulo());
        dto.setDescripcion(encuesta.getDescripcion());
        dto.setFechaInicio(encuesta.getFechaInicio());
        dto.setFechaFin(encuesta.getFechaFin());
        dto.setEstado(encuesta.getEstado());
        dto.setEmpresaId(encuesta.getEmpresa() != null ? encuesta.getEmpresa().getId() : null);
        return dto;
    }

    private Encuesta convertirAEntidad(EncuestaDTO dto) {
        Encuesta encuesta = new Encuesta();
        encuesta.setTitulo(dto.getTitulo());
        encuesta.setDescripcion(dto.getDescripcion());
        encuesta.setFechaInicio(dto.getFechaInicio());
        encuesta.setFechaFin(dto.getFechaFin());
        encuesta.setEstado(dto.getEstado() != null ? dto.getEstado() : "BORRADOR");
        return encuesta;
    }
}
