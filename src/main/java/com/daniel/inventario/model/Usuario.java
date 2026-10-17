package com.daniel.inventario.model;

import java.time.LocalDateTime;

public class Usuario {
    private Long id;
    private LocalDateTime creadoEn;
    private String usuario;
    private String nombre;
    private String apellido;

    public Usuario(Long id, String usuario, String nombre, String apellido) {
        this(id, usuario, nombre, apellido, null);
    }

    public Usuario(Long id, String usuario, String nombre, String apellido, LocalDateTime creadoEn) {
        this.id = id;
        this.creadoEn = creadoEn;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
}
