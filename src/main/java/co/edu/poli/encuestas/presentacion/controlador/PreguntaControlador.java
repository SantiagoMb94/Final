package co.edu.poli.encuestas.presentacion.controlador;

import co.edu.poli.encuestas.modelo.Pregunta;
import co.edu.poli.encuestas.negocio.servicio.PreguntaServicio;
import co.edu.poli.encuestas.presentacion.dto.PreguntaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/preguntas")
@RequiredArgsConstructor
public class PreguntaControlador {

    private final PreguntaServicio preguntaServicio;

    @GetMapping
    public ResponseEntity<List<PreguntaDTO>> listarTodas(@RequestParam(required = false) java.util.UUID encuestaId) {
        List<Pregunta> preguntas;
        if (encuestaId != null) {
            preguntas = preguntaServicio.listarPorEncuesta(encuestaId);
        } else {
            preguntas = preguntaServicio.listarTodas();
        }

        List<PreguntaDTO> preguntasDTO = preguntas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(preguntasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreguntaDTO> buscarPorId(@PathVariable java.util.UUID id) {
        return preguntaServicio.buscarPorId(id)
                .map(pregunta -> ResponseEntity.ok(convertirADTO(pregunta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PreguntaDTO> crear(@Valid @RequestBody PreguntaDTO preguntaDTO) {
        if (preguntaDTO.getEncuestaId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Pregunta pregunta = convertirAEntidad(preguntaDTO);
        Pregunta preguntaCreada = preguntaServicio.crear(pregunta, preguntaDTO.getEncuestaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(preguntaCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreguntaDTO> actualizar(@PathVariable java.util.UUID id,
            @Valid @RequestBody PreguntaDTO preguntaDTO) {
        Pregunta pregunta = convertirAEntidad(preguntaDTO);
        Pregunta preguntaActualizada = preguntaServicio.actualizar(id, pregunta);
        return ResponseEntity.ok(convertirADTO(preguntaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable java.util.UUID id) {
        preguntaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private PreguntaDTO convertirADTO(Pregunta pregunta) {
        PreguntaDTO dto = new PreguntaDTO();
        dto.setId(pregunta.getId());
        dto.setTexto(pregunta.getTexto());
        dto.setTipo(pregunta.getTipo());
        dto.setObligatoria(pregunta.getObligatoria());
        dto.setOrden(pregunta.getOrden());
        dto.setEncuestaId(pregunta.getEncuesta() != null ? pregunta.getEncuesta().getId() : null);
        return dto;
    }

    private Pregunta convertirAEntidad(PreguntaDTO dto) {
        Pregunta pregunta = new Pregunta();
        pregunta.setTexto(dto.getTexto());
        pregunta.setTipo(dto.getTipo());
        pregunta.setObligatoria(dto.getObligatoria() != null ? dto.getObligatoria() : false);
        pregunta.setOrden(dto.getOrden());
        return pregunta;
    }
}
