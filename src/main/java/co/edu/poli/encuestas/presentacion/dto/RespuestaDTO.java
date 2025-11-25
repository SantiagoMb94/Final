package co.edu.poli.encuestas.presentacion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RespuestaDTO {
    
    private Long id;
    
    @Size(max = 1000, message = "El valor de la respuesta no puede exceder 1000 caracteres")
    private String valor;
    
    private LocalDateTime fechaRespuesta;
    
    @Size(max = 100, message = "El identificador del respondente no puede exceder 100 caracteres")
    private String identificadorRespondente;
    
    @NotNull(message = "La respuesta debe estar asociada a una encuesta")
    private Long encuestaId;
    
    @NotNull(message = "La respuesta debe estar asociada a una pregunta")
    private Long preguntaId;
    
    private Long opcionId;
}

