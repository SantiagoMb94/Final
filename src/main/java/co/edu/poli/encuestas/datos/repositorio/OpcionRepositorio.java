package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionRepositorio extends JpaRepository<Opcion, java.util.UUID> {

    List<Opcion> findByPreguntaId(java.util.UUID preguntaId);

    List<Opcion> findByPreguntaIdOrderByOrdenAsc(java.util.UUID preguntaId);
}
