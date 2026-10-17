package com.daniel.inventario.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Producto {
    private Long id;
    private LocalDateTime creadoEn;
    private String codigo;
    private String nombre;
    private BigDecimal precio;
    private int stock;
    private Categoria categoria;
    private Proveedor proveedor;
    private boolean activo;

    public Producto(Long id, String codigo, String nombre, BigDecimal precio, int stock,
            Categoria categoria, Proveedor proveedor, boolean activo) {
        this(id, codigo, nombre, precio, stock, categoria, proveedor, activo, null);
    }

    public Producto(Long id, String codigo, String nombre, BigDecimal precio, int stock,
            Categoria categoria, Proveedor proveedor, boolean activo, LocalDateTime creadoEn) {
        this.id = id;
        this.creadoEn = creadoEn;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.activo = activo;
    }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
    public Categoria getCategoria() { return categoria; }
    public Proveedor getProveedor() { return proveedor; }
    public boolean isActivo() { return activo; }
}
