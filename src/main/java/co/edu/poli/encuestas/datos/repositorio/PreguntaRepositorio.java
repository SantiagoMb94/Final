package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepositorio extends JpaRepository<Pregunta, java.util.UUID> {

    List<Pregunta> findByEncuestaId(java.util.UUID encuestaId);

    List<Pregunta> findByEncuestaIdOrderByOrdenAsc(java.util.UUID encuestaId);
}
