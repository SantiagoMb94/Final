package co.edu.poli.encuestas.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresa")
@Getter
@Setter
public class Empresa extends EntidadBase {
    
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @Size(max = 100, message = "El NIT no puede exceder 100 caracteres")
    @Column(name = "nit", unique = true)
    private String nit;
    
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Column(name = "direccion")
    private String direccion;
    
    @Size(max = 50, message = "El teléfono no puede exceder 50 caracteres")
    @Column(name = "telefono")
    private String telefono;
    
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "email")
    private String email;
    
    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
    
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios = new ArrayList<>();
    
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Encuesta> encuestas = new ArrayList<>();
}

