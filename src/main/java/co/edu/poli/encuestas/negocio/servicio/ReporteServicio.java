package co.edu.poli.encuestas.negocio.servicio;

import co.edu.poli.encuestas.datos.repositorio.EncuestaRepositorio;
import co.edu.poli.encuestas.datos.repositorio.PreguntaRepositorio;
import co.edu.poli.encuestas.datos.repositorio.RespuestaRepositorio;
import co.edu.poli.encuestas.modelo.Encuesta;
import co.edu.poli.encuestas.modelo.Pregunta;
import co.edu.poli.encuestas.modelo.Respuesta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServicio {
    
    private final EncuestaRepositorio encuestaRepositorio;
    private final PreguntaRepositorio preguntaRepositorio;
    private final RespuestaRepositorio respuestaRepositorio;
    private final RespuestaServicio respuestaServicio;
    
    public Map<String, Object> generarReporteBasico(Long encuestaId) {
        Encuesta encuesta = encuestaRepositorio.findById(encuestaId)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada con id: " + encuestaId));
        
        List<Pregunta> preguntas = preguntaRepositorio.findByEncuestaIdOrderByOrdenAsc(encuestaId);
        Long totalRespondentes = respuestaServicio.contarRespondentesPorEncuesta(encuestaId);
        
        Map<String, Object> reporte = new HashMap<>();
        Map<String, Object> encuestaMap = new HashMap<>();
        encuestaMap.put("id", encuesta.getId());
        encuestaMap.put("titulo", encuesta.getTitulo());
        encuestaMap.put("descripcion", encuesta.getDescripcion());
        encuestaMap.put("estado", encuesta.getEstado());
        encuestaMap.put("fechaInicio", encuesta.getFechaInicio());
        encuestaMap.put("fechaFin", encuesta.getFechaFin());
        reporte.put("encuesta", encuestaMap);
        reporte.put("totalRespondentes", totalRespondentes);
        
        List<Map<String, Object>> estadisticasPreguntas = preguntas.stream()
                .map(pregunta -> {
                    List<Respuesta> respuestas = respuestaRepositorio.findByPreguntaId(pregunta.getId());
                    Long totalRespuestas = respuestaServicio.contarRespuestasPorPregunta(pregunta.getId());
                    
                    Map<String, Object> estadistica = new HashMap<>();
                    estadistica.put("preguntaId", pregunta.getId());
                    estadistica.put("texto", pregunta.getTexto());
                    estadistica.put("tipo", pregunta.getTipo());
                    estadistica.put("totalRespuestas", totalRespuestas);
                    
                    // Si es opción múltiple, contar por opción
                    if ("OPCION_MULTIPLE".equals(pregunta.getTipo())) {
                        Map<String, Long> conteoOpciones = respuestas.stream()
                                .filter(r -> r.getOpcion() != null)
                                .collect(Collectors.groupingBy(
                                        r -> r.getOpcion().getTexto(),
                                        Collectors.counting()
                                ));
                        estadistica.put("conteoOpciones", conteoOpciones);
                    }
                    
                    return estadistica;
                })
                .collect(Collectors.toList());
        
        reporte.put("estadisticasPreguntas", estadisticasPreguntas);
        
        return reporte;
    }
    
    public Map<String, Object> generarReportePorEmpresa(Long empresaId) {
        List<Encuesta> encuestas = encuestaRepositorio.findByEmpresaId(empresaId);
        
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("empresaId", empresaId);
        reporte.put("totalEncuestas", encuestas.size());
        
        List<Map<String, Object>> resumenEncuestas = encuestas.stream()
                .map(encuesta -> {
                    Long totalRespondentes = respuestaServicio.contarRespondentesPorEncuesta(encuesta.getId());
                    Map<String, Object> resumen = new HashMap<>();
                    resumen.put("id", encuesta.getId());
                    resumen.put("titulo", encuesta.getTitulo());
                    resumen.put("estado", encuesta.getEstado());
                    resumen.put("totalRespondentes", totalRespondentes);
                    return resumen;
                })
                .collect(Collectors.toList());
        
        reporte.put("encuestas", resumenEncuestas);
        
        return reporte;
    }
}

