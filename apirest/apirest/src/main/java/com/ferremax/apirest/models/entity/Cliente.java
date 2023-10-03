package com.ferremax.apirest.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;

@Entity
@Getter//sirve para generar los getter
@Setter//sirve para generar los setter
                                         //@FieldDefaults(level = AccessLevel.PACKAGE)//con esta notación evitamos definir los atrivutos ya sea como privado o publico
@Table(name = "Clientes")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
    @NotEmpty(message="No puede estar vacio")
    @Size(min=4, max = 15,message = "El tamaño tiene que estar entre 4 y 15 caracteres")
    @Column(nullable = false)
     private String nombre;
    @NotEmpty(message = "No puede estar vacio")
    private String apellido;
    @NotEmpty(message = "No puede estar vacio")
    @Email(message = "No es una direccion de correo valida")
    @Column(nullable = false, unique = true)
     private String email;
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
     private Date creteAt;
    @PrePersist
    public void prePersist(){
        creteAt= new Date();
    }


    private static final long serialVersionUID=1L;
}
