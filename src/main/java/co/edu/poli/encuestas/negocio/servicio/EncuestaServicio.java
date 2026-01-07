package co.edu.poli.encuestas.negocio.servicio;

import co.edu.poli.encuestas.datos.repositorio.EmpresaRepositorio;
import co.edu.poli.encuestas.datos.repositorio.EncuestaRepositorio;
import co.edu.poli.encuestas.modelo.Empresa;
import co.edu.poli.encuestas.modelo.Encuesta;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EncuestaServicio {

    private final EncuestaRepositorio encuestaRepositorio;
    private final EmpresaRepositorio empresaRepositorio;

    public List<Encuesta> listarTodas() {
        return encuestaRepositorio.findAll();
    }

    public List<Encuesta> listarPorEmpresa(java.util.UUID empresaId) {
        return encuestaRepositorio.findByEmpresaId(empresaId);
    }

    public List<Encuesta> listarPorEmpresaYEstado(java.util.UUID empresaId, String estado) {
        return encuestaRepositorio.findByEmpresaIdAndEstado(empresaId, estado);
    }

    public Optional<Encuesta> buscarPorId(java.util.UUID id) {
        return encuestaRepositorio.findById(id);
    }

    @Transactional
    public Encuesta crear(Encuesta encuesta, java.util.UUID empresaId) {
        Empresa empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + empresaId));

        encuesta.setEmpresa(empresa);
        if (encuesta.getFechaInicio() == null) {
            encuesta.setFechaInicio(LocalDateTime.now());
        }
        if (encuesta.getEstado() == null || encuesta.getEstado().isEmpty()) {
            encuesta.setEstado("BORRADOR");
        }

        return encuestaRepositorio.save(encuesta);
    }

    @Transactional
    public Encuesta actualizar(java.util.UUID id, Encuesta encuestaActualizada) {
        return encuestaRepositorio.findById(id)
                .map(encuesta -> {
                    encuesta.setTitulo(encuestaActualizada.getTitulo());
                    encuesta.setDescripcion(encuestaActualizada.getDescripcion());
                    encuesta.setFechaInicio(encuestaActualizada.getFechaInicio());
                    encuesta.setFechaFin(encuestaActualizada.getFechaFin());
                    encuesta.setEstado(encuestaActualizada.getEstado());
                    return encuestaRepositorio.save(encuesta);
                })
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada con id: " + id));
    }

    @Transactional
    public void eliminar(java.util.UUID id) {
        encuestaRepositorio.deleteById(id);
    }

    @Transactional
    public Encuesta activar(java.util.UUID id) {
        return encuestaRepositorio.findById(id)
                .map(encuesta -> {
                    encuesta.setEstado("ACTIVA");
                    if (encuesta.getFechaInicio() == null) {
                        encuesta.setFechaInicio(LocalDateTime.now());
                    }
                    return encuestaRepositorio.save(encuesta);
                })
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada con id: " + id));
    }

    @Transactional
    public Encuesta finalizar(java.util.UUID id) {
        return encuestaRepositorio.findById(id)
                .map(encuesta -> {
                    encuesta.setEstado("FINALIZADA");
                    if (encuesta.getFechaFin() == null) {
                        encuesta.setFechaFin(LocalDateTime.now());
                    }
                    return encuestaRepositorio.save(encuesta);
                })
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada con id: " + id));
    }
}
