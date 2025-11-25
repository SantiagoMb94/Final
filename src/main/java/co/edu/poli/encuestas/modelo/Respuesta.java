package co.edu.poli.encuestas.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "respuesta")
@Getter
@Setter
public class Respuesta extends EntidadBase {
    
    @Size(max = 1000, message = "El valor de la respuesta no puede exceder 1000 caracteres")
    @Column(name = "valor", length = 1000)
    private String valor;
    
    @Column(name = "fecha_respuesta", nullable = false)
    private LocalDateTime fechaRespuesta;
    
    @Size(max = 100, message = "El identificador del respondente no puede exceder 100 caracteres")
    @Column(name = "identificador_respondente")
    private String identificadorRespondente; // Email, ID, etc.
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuesta_id", nullable = false)
    @NotNull(message = "La respuesta debe estar asociada a una encuesta")
    private Encuesta encuesta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id", nullable = false)
    @NotNull(message = "La respuesta debe estar asociada a una pregunta")
    private Pregunta pregunta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcion_id")
    private Opcion opcion; // Para respuestas de opción múltiple
}

