package co.edu.poli.encuestas.datos.repositorio;

import co.edu.poli.encuestas.modelo.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, Long> {
    
    Optional<Empresa> findByNit(String nit);
    
    Optional<Empresa> findByNombre(String nombre);
    
    List<Empresa> findByActivaTrue();
}

