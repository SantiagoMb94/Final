package co.edu.poli.encuestas.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OpcionDTO {
    
    private Long id;
    
    @NotBlank(message = "El texto de la opción es obligatorio")
    @Size(max = 255, message = "El texto no puede exceder 255 caracteres")
    private String texto;
    
    private Integer orden;
    
    @NotNull(message = "La opción debe estar asociada a una pregunta")
    private Long preguntaId;
}

