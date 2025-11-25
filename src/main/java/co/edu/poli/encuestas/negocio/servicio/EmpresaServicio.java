package co.edu.poli.encuestas.negocio.servicio;

import co.edu.poli.encuestas.datos.repositorio.EmpresaRepositorio;
import co.edu.poli.encuestas.modelo.Empresa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaServicio {
    
    private final EmpresaRepositorio empresaRepositorio;
    
    public List<Empresa> listarTodas() {
        return empresaRepositorio.findAll();
    }
    
    public List<Empresa> listarActivas() {
        return empresaRepositorio.findByActivaTrue();
    }
    
    public Optional<Empresa> buscarPorId(Long id) {
        return empresaRepositorio.findById(id);
    }
    
    public Optional<Empresa> buscarPorNit(String nit) {
        return empresaRepositorio.findByNit(nit);
    }
    
    @Transactional
    public Empresa crear(Empresa empresa) {
        return empresaRepositorio.save(empresa);
    }
    
    @Transactional
    public Empresa actualizar(Long id, Empresa empresaActualizada) {
        return empresaRepositorio.findById(id)
                .map(empresa -> {
                    empresa.setNombre(empresaActualizada.getNombre());
                    empresa.setNit(empresaActualizada.getNit());
                    empresa.setDireccion(empresaActualizada.getDireccion());
                    empresa.setTelefono(empresaActualizada.getTelefono());
                    empresa.setEmail(empresaActualizada.getEmail());
                    empresa.setActiva(empresaActualizada.getActiva());
                    return empresaRepositorio.save(empresa);
                })
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + id));
    }
    
    @Transactional
    public void eliminar(Long id) {
        empresaRepositorio.deleteById(id);
    }
    
    @Transactional
    public void desactivar(Long id) {
        empresaRepositorio.findById(id)
                .ifPresent(empresa -> {
                    empresa.setActiva(false);
                    empresaRepositorio.save(empresa);
                });
    }
}

