package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepositorio extends JpaRepository<Respuesta, java.util.UUID> {

    List<Respuesta> findByEncuestaId(java.util.UUID encuestaId);

    List<Respuesta> findByPreguntaId(java.util.UUID preguntaId);

    List<Respuesta> findByEncuestaIdAndIdentificadorRespondente(java.util.UUID encuestaId,
            String identificadorRespondente);

    @Query("SELECT COUNT(DISTINCT r.identificadorRespondente) FROM Respuesta r WHERE r.encuesta.id = :encuestaId")
    Long countDistinctRespondentesByEncuestaId(@Param("encuestaId") java.util.UUID encuestaId);

    @Query("SELECT COUNT(r) FROM Respuesta r WHERE r.pregunta.id = :preguntaId")
    Long countByPreguntaId(@Param("preguntaId") java.util.UUID preguntaId);
}
