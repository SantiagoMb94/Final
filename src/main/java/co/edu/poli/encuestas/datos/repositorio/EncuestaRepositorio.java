package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncuestaRepositorio extends JpaRepository<Encuesta, java.util.UUID> {

    List<Encuesta> findByEmpresaId(java.util.UUID empresaId);

    List<Encuesta> findByEmpresaIdAndEstado(java.util.UUID empresaId, String estado);
}
