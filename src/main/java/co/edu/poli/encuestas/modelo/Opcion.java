package co.edu.poli.encuestas.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "opcion")
@Getter
@Setter
public class Opcion extends EntidadBase {
    
    @NotBlank(message = "El texto de la opción es obligatorio")
    @Size(max = 255, message = "El texto no puede exceder 255 caracteres")
    @Column(name = "texto", nullable = false)
    private String texto;
    
    @Column(name = "orden")
    private Integer orden;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id", nullable = false)
    @NotNull(message = "La opción debe estar asociada a una pregunta")
    private Pregunta pregunta;
}

