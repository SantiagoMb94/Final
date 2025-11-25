package co.edu.poli.encuestas.negocio.servicio;

import co.edu.poli.encuestas.datos.repositorio.EncuestaRepositorio;
import co.edu.poli.encuestas.datos.repositorio.OpcionRepositorio;
import co.edu.poli.encuestas.datos.repositorio.PreguntaRepositorio;
import co.edu.poli.encuestas.datos.repositorio.RespuestaRepositorio;
import co.edu.poli.encuestas.modelo.Encuesta;
import co.edu.poli.encuestas.modelo.Opcion;
import co.edu.poli.encuestas.modelo.Pregunta;
import co.edu.poli.encuestas.modelo.Respuesta;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RespuestaServicio {
    
    private final RespuestaRepositorio respuestaRepositorio;
    private final EncuestaRepositorio encuestaRepositorio;
    private final PreguntaRepositorio preguntaRepositorio;
    private final OpcionRepositorio opcionRepositorio;
    
    public List<Respuesta> listarTodas() {
        return respuestaRepositorio.findAll();
    }
    
    public List<Respuesta> listarPorEncuesta(Long encuestaId) {
        return respuestaRepositorio.findByEncuestaId(encuestaId);
    }
    
    public List<Respuesta> listarPorPregunta(Long preguntaId) {
        return respuestaRepositorio.findByPreguntaId(preguntaId);
    }
    
    public List<Respuesta> listarPorEncuestaYRespondente(Long encuestaId, String identificadorRespondente) {
        return respuestaRepositorio.findByEncuestaIdAndIdentificadorRespondente(encuestaId, identificadorRespondente);
    }
    
    public Optional<Respuesta> buscarPorId(Long id) {
        return respuestaRepositorio.findById(id);
    }
    
    @Transactional
    public Respuesta crear(Respuesta respuesta, Long encuestaId, Long preguntaId, Long opcionId) {
        Encuesta encuesta = encuestaRepositorio.findById(encuestaId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada con id: " + encuestaId));
        
        Pregunta pregunta = preguntaRepositorio.findById(preguntaId)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + preguntaId));
        
        respuesta.setEncuesta(encuesta);
        respuesta.setPregunta(pregunta);
        
        if (opcionId != null) {
            Opcion opcion = opcionRepositorio.findById(opcionId)
                    .orElseThrow(() -> new RuntimeException("Opción no encontrada con id: " + opcionId));
            respuesta.setOpcion(opcion);
        }
        
        if (respuesta.getFechaRespuesta() == null) {
            respuesta.setFechaRespuesta(LocalDateTime.now());
        }
        
        return respuestaRepositorio.save(respuesta);
    }
    
    @Transactional
    public Respuesta actualizar(Long id, Respuesta respuestaActualizada) {
        return respuestaRepositorio.findById(id)
                .map(respuesta -> {
                    respuesta.setValor(respuestaActualizada.getValor());
                    respuesta.setIdentificadorRespondente(respuestaActualizada.getIdentificadorRespondente());
                    if (respuestaActualizada.getOpcion() != null) {
                        respuesta.setOpcion(respuestaActualizada.getOpcion());
                    }
                    return respuestaRepositorio.save(respuesta);
                })
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada con id: " + id));
    }
    
    @Transactional
    public void eliminar(Long id) {
        respuestaRepositorio.deleteById(id);
    }
    
    public Long contarRespondentesPorEncuesta(Long encuestaId) {
        return respuestaRepositorio.countDistinctRespondentesByEncuestaId(encuestaId);
    }
    
    public Long contarRespuestasPorPregunta(Long preguntaId) {
        return respuestaRepositorio.countByPreguntaId(preguntaId);
    }
}

