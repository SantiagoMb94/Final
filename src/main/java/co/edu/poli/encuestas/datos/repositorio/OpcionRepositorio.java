package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionRepositorio extends JpaRepository<Opcion, Long> {
    
    List<Opcion> findByPreguntaId(Long preguntaId);
    
    List<Opcion> findByPreguntaIdOrderByOrdenAsc(Long preguntaId);
}

