package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByEmpresaId(Long empresaId);
    
    List<Usuario> findByEmpresaIdAndActivoTrue(Long empresaId);
    
    boolean existsByEmail(String email);
}

