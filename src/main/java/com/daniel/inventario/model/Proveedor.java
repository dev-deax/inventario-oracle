package com.daniel.inventario.model;

public class Proveedor {
    private Long id;
    private String nombre;
    private String empresa;
    private String nit;
    private String telefono;
    private String email;

    public Proveedor(Long id, String nombre, String empresa, String nit, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.empresa = empresa;
        this.nit = nit;
        this.telefono = telefono;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public String getNit() { return nit; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return nombre; }
}
