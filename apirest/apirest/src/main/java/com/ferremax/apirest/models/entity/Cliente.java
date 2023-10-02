package com.ferremax.apirest.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreteAt() {
        return creteAt;
    }

    public void setCreteAt(Date creteAt) {
        this.creteAt = creteAt;
    }
    private static final long serialVersionUID=1L;
}
