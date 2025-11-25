package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncuestaRepositorio extends JpaRepository<Encuesta, Long> {
    
    List<Encuesta> findByEmpresaId(Long empresaId);
    
    List<Encuesta> findByEmpresaIdAndEstado(Long empresaId, String estado);
}

