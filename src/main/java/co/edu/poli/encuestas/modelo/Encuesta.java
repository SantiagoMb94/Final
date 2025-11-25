package co.edu.poli.encuestas.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "encuesta")
@Getter
@Setter
public class Encuesta extends EntidadBase {
    
    @NotBlank(message = "El título de la encuesta es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    @Column(name = "titulo", nullable = false)
    private String titulo;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    @Column(name = "estado", nullable = false)
    private String estado = "BORRADOR"; // BORRADOR, ACTIVA, FINALIZADA
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
    
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pregunta> preguntas = new ArrayList<>();
    
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas = new ArrayList<>();
}

