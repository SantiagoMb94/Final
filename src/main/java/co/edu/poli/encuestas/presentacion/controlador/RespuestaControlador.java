package co.edu.poli.encuestas.presentacion.controlador;

import co.edu.poli.encuestas.modelo.Respuesta;
import co.edu.poli.encuestas.negocio.servicio.RespuestaServicio;
import co.edu.poli.encuestas.presentacion.dto.RespuestaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/respuestas")
@RequiredArgsConstructor
public class RespuestaControlador {
    
    private final RespuestaServicio respuestaServicio;
    
    @GetMapping
    public ResponseEntity<List<RespuestaDTO>> listarTodas(
            @RequestParam(required = false) Long encuestaId,
            @RequestParam(required = false) Long preguntaId,
            @RequestParam(required = false) String identificadorRespondente) {
        List<Respuesta> respuestas;
        if (encuestaId != null && identificadorRespondente != null) {
            respuestas = respuestaServicio.listarPorEncuestaYRespondente(encuestaId, identificadorRespondente);
        } else if (encuestaId != null) {
            respuestas = respuestaServicio.listarPorEncuesta(encuestaId);
        } else if (preguntaId != null) {
            respuestas = respuestaServicio.listarPorPregunta(preguntaId);
        } else {
            respuestas = respuestaServicio.listarTodas();
        }
        
        List<RespuestaDTO> respuestasDTO = respuestas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(respuestasDTO);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RespuestaDTO> buscarPorId(@PathVariable Long id) {
        return respuestaServicio.buscarPorId(id)
                .map(respuesta -> ResponseEntity.ok(convertirADTO(respuesta)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<RespuestaDTO> crear(@Valid @RequestBody RespuestaDTO respuestaDTO) {
        if (respuestaDTO.getEncuestaId() == null || respuestaDTO.getPreguntaId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Respuesta respuesta = convertirAEntidad(respuestaDTO);
        Respuesta respuestaCreada = respuestaServicio.crear(
                respuesta,
                respuestaDTO.getEncuestaId(),
                respuestaDTO.getPreguntaId(),
                respuestaDTO.getOpcionId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(respuestaCreada));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RespuestaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RespuestaDTO respuestaDTO) {
        Respuesta respuesta = convertirAEntidad(respuestaDTO);
        Respuesta respuestaActualizada = respuestaServicio.actualizar(id, respuesta);
        return ResponseEntity.ok(convertirADTO(respuestaActualizada));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        respuestaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    private RespuestaDTO convertirADTO(Respuesta respuesta) {
        RespuestaDTO dto = new RespuestaDTO();
        dto.setId(respuesta.getId());
        dto.setValor(respuesta.getValor());
        dto.setFechaRespuesta(respuesta.getFechaRespuesta());
        dto.setIdentificadorRespondente(respuesta.getIdentificadorRespondente());
        dto.setEncuestaId(respuesta.getEncuesta() != null ? respuesta.getEncuesta().getId() : null);
        dto.setPreguntaId(respuesta.getPregunta() != null ? respuesta.getPregunta().getId() : null);
        dto.setOpcionId(respuesta.getOpcion() != null ? respuesta.getOpcion().getId() : null);
        return dto;
    }
    
    private Respuesta convertirAEntidad(RespuestaDTO dto) {
        Respuesta respuesta = new Respuesta();
        respuesta.setValor(dto.getValor());
        respuesta.setIdentificadorRespondente(dto.getIdentificadorRespondente());
        return respuesta;
    }
}

