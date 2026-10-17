package com.daniel.inventario.model;

import java.time.LocalDateTime;

public class Proveedor {
    private Long id;
    private LocalDateTime creadoEn;
    private String nombre;
    private String empresa;
    private String nit;
    private String telefono;
    private String email;

    public Proveedor(Long id, String nombre, String empresa, String nit, String telefono, String email) {
        this(id, nombre, empresa, nit, telefono, email, null);
    }

    public Proveedor(Long id, String nombre, String empresa, String nit, String telefono, String email, LocalDateTime creadoEn) {
        this.id = id;
        this.creadoEn = creadoEn;
        this.nombre = nombre;
        this.empresa = empresa;
        this.nit = nit;
        this.telefono = telefono;
        this.email = email;
    }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public String getNit() { return nit; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return nombre; }
}
