package co.edu.poli.encuestas.presentacion.controlador;

import co.edu.poli.encuestas.negocio.servicio.ReporteServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteControlador {

    private final ReporteServicio reporteServicio;

    @GetMapping("/encuestas/{encuestaId}")
    public ResponseEntity<Map<String, Object>> generarReporteBasico(@PathVariable java.util.UUID encuestaId) {
        Map<String, Object> reporte = reporteServicio.generarReporteBasico(encuestaId);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/empresas/{empresaId}")
    public ResponseEntity<Map<String, Object>> generarReportePorEmpresa(@PathVariable java.util.UUID empresaId) {
        Map<String, Object> reporte = reporteServicio.generarReportePorEmpresa(empresaId);
        return ResponseEntity.ok(reporte);
    }
}
