package com.daniel.inventario.model;

import java.time.LocalDateTime;

public class Categoria {
    private Long id;
    private LocalDateTime creadoEn;
    private String nombre;
    private String descripcion;

    public Categoria(Long id, String nombre, String descripcion) {
        this(id, nombre, descripcion, null);
    }

    public Categoria(Long id, String nombre, String descripcion, LocalDateTime creadoEn) {
        this.id = id;
        this.creadoEn = creadoEn;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() { return nombre; }
}
