package com.daniel.inventario.model;

public class Usuario {
    private Long id;
    private String usuario;
    private String nombre;
    private String apellido;

    public Usuario(Long id, String usuario, String nombre, String apellido) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
}
