package co.edu.poli.encuestas.negocio.servicio;

import co.edu.poli.encuestas.datos.repositorio.OpcionRepositorio;
import co.edu.poli.encuestas.datos.repositorio.PreguntaRepositorio;
import co.edu.poli.encuestas.modelo.Opcion;
import co.edu.poli.encuestas.modelo.Pregunta;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpcionServicio {

    private final OpcionRepositorio opcionRepositorio;
    private final PreguntaRepositorio preguntaRepositorio;

    public List<Opcion> listarTodas() {
        return opcionRepositorio.findAll();
    }

    public List<Opcion> listarPorPregunta(java.util.UUID preguntaId) {
        return opcionRepositorio.findByPreguntaIdOrderByOrdenAsc(preguntaId);
    }

    public Optional<Opcion> buscarPorId(java.util.UUID id) {
        return opcionRepositorio.findById(id);
    }

    @Transactional
    public Opcion crear(Opcion opcion, java.util.UUID preguntaId) {
        Pregunta pregunta = preguntaRepositorio.findById(preguntaId)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + preguntaId));

        opcion.setPregunta(pregunta);
        return opcionRepositorio.save(opcion);
    }

    @Transactional
    public Opcion actualizar(java.util.UUID id, Opcion opcionActualizada) {
        return opcionRepositorio.findById(id)
                .map(opcion -> {
                    opcion.setTexto(opcionActualizada.getTexto());
                    opcion.setOrden(opcionActualizada.getOrden());
                    return opcionRepositorio.save(opcion);
                })
                .orElseThrow(() -> new RuntimeException("Opción no encontrada con id: " + id));
    }

    @Transactional
    public void eliminar(java.util.UUID id) {
        opcionRepositorio.deleteById(id);
    }
}
