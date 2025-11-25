package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepositorio extends JpaRepository<Respuesta, Long> {
    
    List<Respuesta> findByEncuestaId(Long encuestaId);
    
    List<Respuesta> findByPreguntaId(Long preguntaId);
    
    List<Respuesta> findByEncuestaIdAndIdentificadorRespondente(Long encuestaId, String identificadorRespondente);
    
    @Query("SELECT COUNT(DISTINCT r.identificadorRespondente) FROM Respuesta r WHERE r.encuesta.id = :encuestaId")
    Long countDistinctRespondentesByEncuestaId(@Param("encuestaId") Long encuestaId);
    
    @Query("SELECT COUNT(r) FROM Respuesta r WHERE r.pregunta.id = :preguntaId")
    Long countByPreguntaId(@Param("preguntaId") Long preguntaId);
}

