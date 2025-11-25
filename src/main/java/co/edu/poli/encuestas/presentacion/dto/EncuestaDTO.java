package co.edu.poli.encuestas.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EncuestaDTO {
    
    private Long id;
    
    @NotBlank(message = "El título de la encuesta es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    private LocalDateTime fechaInicio;
    
    private LocalDateTime fechaFin;
    
    private String estado;
    
    private Long empresaId;
}

