package com.daniel.inventario.dao;

import com.daniel.inventario.db.ConnectionManager;
import com.daniel.inventario.model.Categoria;
import com.daniel.inventario.model.Producto;
import com.daniel.inventario.model.Proveedor;
import com.daniel.inventario.util.Validators;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class ProductoDAO {
    public ArrayList<Producto> listar() throws SQLException {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id, p.codigo, p.nombre, p.precio, p.stock, p.activo, "
                + "c.id categoria_id, c.nombre categoria_nombre, c.descripcion, "
                + "v.id proveedor_id, v.nombre proveedor_nombre, v.empresa, v.nit, v.telefono, v.email "
                + "FROM productos p JOIN categorias c ON c.id = p.categoria_id "
                + "JOIN proveedores v ON v.id = p.proveedor_id ORDER BY p.creado_en DESC, p.id DESC";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql);
                ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                Categoria categoria = new Categoria(resultado.getLong("categoria_id"),
                        resultado.getString("categoria_nombre"), resultado.getString("descripcion"));
                Proveedor proveedor = new Proveedor(resultado.getLong("proveedor_id"),
                        resultado.getString("proveedor_nombre"), resultado.getString("empresa"),
                        resultado.getString("nit"), resultado.getString("telefono"), resultado.getString("email"));
                productos.add(new Producto(resultado.getLong("id"), resultado.getString("codigo"),
                        resultado.getString("nombre"), resultado.getBigDecimal("precio"),
                        resultado.getInt("stock"), categoria, proveedor, resultado.getInt("activo") == 1));
            }
        }
        return productos;
    }

    public void insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo) "
                + "VALUES (productos_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignar(consulta, producto);
            consulta.executeUpdate();
        }
    }

    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET codigo = ?, nombre = ?, precio = ?, stock = ?, "
                + "categoria_id = ?, proveedor_id = ?, activo = ? WHERE id = ?";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignar(consulta, producto);
            consulta.setLong(8, producto.getId());
            if (consulta.executeUpdate() == 0) {
                throw new SQLException("El producto ya no existe. Actualiza la lista.");
            }
        }
    }

    private void asignar(PreparedStatement consulta, Producto producto) throws SQLException {
        if (producto.getCategoria() == null || producto.getProveedor() == null) {
            throw new IllegalArgumentException("Selecciona una categoría y un proveedor.");
        }
        consulta.setString(1, Validators.requerido(producto.getCodigo(), "Código", 40).toUpperCase(Locale.ROOT));
        consulta.setString(2, Validators.requerido(producto.getNombre(), "Nombre", 150));
        consulta.setBigDecimal(3, Validators.precio(producto.getPrecio().toPlainString()));
        consulta.setInt(4, Validators.stock(String.valueOf(producto.getStock())));
        consulta.setLong(5, producto.getCategoria().getId());
        consulta.setLong(6, producto.getProveedor().getId());
        if (producto.isActivo()) {
            consulta.setInt(7, 1);
        } else {
            consulta.setInt(7, 0);
        }
    }

    public void eliminar(long id) throws SQLException {
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement("DELETE FROM productos WHERE id = ?")) {
            consulta.setLong(1, id);
            if (consulta.executeUpdate() == 0) {
                throw new SQLException("El producto ya no existe. Actualiza la lista.");
            }
        }
    }
}
