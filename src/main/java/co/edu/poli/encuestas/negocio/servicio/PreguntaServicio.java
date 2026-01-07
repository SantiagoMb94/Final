package co.edu.poli.encuestas.negocio.servicio;

import co.edu.poli.encuestas.datos.repositorio.EncuestaRepositorio;
import co.edu.poli.encuestas.datos.repositorio.PreguntaRepositorio;
import co.edu.poli.encuestas.modelo.Encuesta;
import co.edu.poli.encuestas.modelo.Pregunta;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreguntaServicio {

    private final PreguntaRepositorio preguntaRepositorio;
    private final EncuestaRepositorio encuestaRepositorio;

    public List<Pregunta> listarTodas() {
        return preguntaRepositorio.findAll();
    }

    public List<Pregunta> listarPorEncuesta(java.util.UUID encuestaId) {
        return preguntaRepositorio.findByEncuestaIdOrderByOrdenAsc(encuestaId);
    }

    public Optional<Pregunta> buscarPorId(java.util.UUID id) {
        return preguntaRepositorio.findById(id);
    }

    @Transactional
    public Pregunta crear(Pregunta pregunta, java.util.UUID encuestaId) {
        Encuesta encuesta = encuestaRepositorio.findById(encuestaId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada con id: " + encuestaId));

        pregunta.setEncuesta(encuesta);
        return preguntaRepositorio.save(pregunta);
    }

    @Transactional
    public Pregunta actualizar(java.util.UUID id, Pregunta preguntaActualizada) {
        return preguntaRepositorio.findById(id)
                .map(pregunta -> {
                    pregunta.setTexto(preguntaActualizada.getTexto());
                    pregunta.setTipo(preguntaActualizada.getTipo());
                    pregunta.setObligatoria(preguntaActualizada.getObligatoria());
                    pregunta.setOrden(preguntaActualizada.getOrden());
                    return preguntaRepositorio.save(pregunta);
                })
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + id));
    }

    @Transactional
    public void eliminar(java.util.UUID id) {
        preguntaRepositorio.deleteById(id);
    }
}
