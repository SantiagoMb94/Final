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
        reporte.put("encuesta", Map.of(
                "id", encuesta.getId(),
                "titulo", encuesta.getTitulo(),
                "descripcion", encuesta.getDescripcion(),
                "estado", encuesta.getEstado(),
                "fechaInicio", encuesta.getFechaInicio(),
                "fechaFin", encuesta.getFechaFin()
        ));
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
                    return Map.of(
                            "id", encuesta.getId(),
                            "titulo", encuesta.getTitulo(),
                            "estado", encuesta.getEstado(),
                            "totalRespondentes", totalRespondentes
                    );
                })
                .collect(Collectors.toList());
        
        reporte.put("encuestas", resumenEncuestas);
        
        return reporte;
    }
}

