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

    public List<Respuesta> listarPorEncuesta(java.util.UUID encuestaId) {
        return respuestaRepositorio.findByEncuestaId(encuestaId);
    }

    public List<Respuesta> listarPorPregunta(java.util.UUID preguntaId) {
        return respuestaRepositorio.findByPreguntaId(preguntaId);
    }

    public List<Respuesta> listarPorEncuestaYRespondente(java.util.UUID encuestaId, String identificadorRespondente) {
        return respuestaRepositorio.findByEncuestaIdAndIdentificadorRespondente(encuestaId, identificadorRespondente);
    }

    public Optional<Respuesta> buscarPorId(java.util.UUID id) {
        return respuestaRepositorio.findById(id);
    }

    @Transactional
    public Respuesta crear(Respuesta respuesta, java.util.UUID encuestaId, java.util.UUID preguntaId,
            java.util.UUID opcionId) {
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
    public Respuesta actualizar(java.util.UUID id, Respuesta respuestaActualizada) {
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
    public void eliminar(java.util.UUID id) {
        respuestaRepositorio.deleteById(id);
    }

    public Long contarRespondentesPorEncuesta(java.util.UUID encuestaId) {
        return respuestaRepositorio.countDistinctRespondentesByEncuestaId(encuestaId);
    }

    public Long contarRespuestasPorPregunta(java.util.UUID preguntaId) {
        return respuestaRepositorio.countByPreguntaId(preguntaId);
    }
}
