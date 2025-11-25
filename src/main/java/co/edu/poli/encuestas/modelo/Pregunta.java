package co.edu.poli.encuestas.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pregunta")
@Getter
@Setter
public class Pregunta extends EntidadBase {
    
    @NotBlank(message = "El texto de la pregunta es obligatorio")
    @Size(max = 500, message = "El texto no puede exceder 500 caracteres")
    @Column(name = "texto", nullable = false, length = 500)
    private String texto;
    
    @Size(max = 20, message = "El tipo no puede exceder 20 caracteres")
    @Column(name = "tipo", nullable = false)
    private String tipo; // OPCION_MULTIPLE, TEXTO_LIBRE, SI_NO, ESCALA
    
    @Column(name = "obligatoria", nullable = false)
    private Boolean obligatoria = false;
    
    @Column(name = "orden")
    private Integer orden;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuesta_id", nullable = false)
    @NotNull(message = "La pregunta debe estar asociada a una encuesta")
    private Encuesta encuesta;
    
    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opcion> opciones = new ArrayList<>();
}

