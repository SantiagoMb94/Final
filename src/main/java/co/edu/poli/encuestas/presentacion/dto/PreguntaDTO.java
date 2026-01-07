package co.edu.poli.encuestas.presentacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PreguntaDTO {

    private java.util.UUID id;

    @NotBlank(message = "El texto de la pregunta es obligatorio")
    @Size(max = 500, message = "El texto no puede exceder 500 caracteres")
    private String texto;

    @NotBlank(message = "El tipo de pregunta es obligatorio")
    @Size(max = 20, message = "El tipo no puede exceder 20 caracteres")
    private String tipo;

    private Boolean obligatoria;

    private Integer orden;

    @NotNull(message = "La pregunta debe estar asociada a una encuesta")
    private java.util.UUID encuestaId;
}
