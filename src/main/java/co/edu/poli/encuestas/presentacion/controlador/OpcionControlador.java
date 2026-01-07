package co.edu.poli.encuestas.presentacion.controlador;

import co.edu.poli.encuestas.modelo.Opcion;
import co.edu.poli.encuestas.negocio.servicio.OpcionServicio;
import co.edu.poli.encuestas.presentacion.dto.OpcionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opciones")
@RequiredArgsConstructor
public class OpcionControlador {

    private final OpcionServicio opcionServicio;

    @GetMapping
    public ResponseEntity<List<OpcionDTO>> listarTodas(@RequestParam(required = false) java.util.UUID preguntaId) {
        List<Opcion> opciones;
        if (preguntaId != null) {
            opciones = opcionServicio.listarPorPregunta(preguntaId);
        } else {
            opciones = opcionServicio.listarTodas();
        }

        List<OpcionDTO> opcionesDTO = opciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(opcionesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpcionDTO> buscarPorId(@PathVariable java.util.UUID id) {
        return opcionServicio.buscarPorId(id)
                .map(opcion -> ResponseEntity.ok(convertirADTO(opcion)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OpcionDTO> crear(@Valid @RequestBody OpcionDTO opcionDTO) {
        if (opcionDTO.getPreguntaId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Opcion opcion = convertirAEntidad(opcionDTO);
        Opcion opcionCreada = opcionServicio.crear(opcion, opcionDTO.getPreguntaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(opcionCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpcionDTO> actualizar(@PathVariable java.util.UUID id,
            @Valid @RequestBody OpcionDTO opcionDTO) {
        Opcion opcion = convertirAEntidad(opcionDTO);
        Opcion opcionActualizada = opcionServicio.actualizar(id, opcion);
        return ResponseEntity.ok(convertirADTO(opcionActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable java.util.UUID id) {
        opcionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private OpcionDTO convertirADTO(Opcion opcion) {
        OpcionDTO dto = new OpcionDTO();
        dto.setId(opcion.getId());
        dto.setTexto(opcion.getTexto());
        dto.setOrden(opcion.getOrden());
        dto.setPreguntaId(opcion.getPregunta() != null ? opcion.getPregunta().getId() : null);
        return dto;
    }

    private Opcion convertirAEntidad(OpcionDTO dto) {
        Opcion opcion = new Opcion();
        opcion.setTexto(dto.getTexto());
        opcion.setOrden(dto.getOrden());
        return opcion;
    }
}
