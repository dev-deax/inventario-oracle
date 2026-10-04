package com.daniel.inventario;

import com.daniel.inventario.dao.CategoriaDAO;
import com.daniel.inventario.dao.ProductoDAO;
import com.daniel.inventario.dao.ProveedorDAO;
import com.daniel.inventario.dao.UsuarioDAO;
import com.daniel.inventario.db.ConnectionManager;
import com.daniel.inventario.model.Categoria;
import com.daniel.inventario.model.Producto;
import com.daniel.inventario.model.Proveedor;
import com.daniel.inventario.model.Usuario;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class OracleIntegrationTest {
    public static void main(String[] args) throws Exception {
        String marca = "test_" + UUID.randomUUID().toString().substring(0, 12);
        String codigo = marca.toUpperCase(java.util.Locale.ROOT);
        UsuarioDAO usuarios = new UsuarioDAO();
        CategoriaDAO categorias = new CategoriaDAO();
        ProveedorDAO proveedores = new ProveedorDAO();
        ProductoDAO productos = new ProductoDAO();
        try {
            usuarios.insertar(new Usuario(null, marca, "Prueba", "Temporal"), "ClavePrueba123".toCharArray());
            Usuario sesion = usuarios.autenticar(marca, "ClavePrueba123".toCharArray());
            comprobar(sesion != null, "Login válido");
            comprobar(usuarios.autenticar(marca, "incorrecta".toCharArray()) == null, "Login incorrecto");
            comprobar(usuarios.autenticar(marca + "x", "ClavePrueba123".toCharArray()) == null, "Usuario inexistente");
            comprobar(usuarios.listar().get(0).getId().equals(sesion.getId()), "Usuario reciente primero");
            boolean duplicado = false;
            try {
                usuarios.insertar(new Usuario(null, marca, "Otro", "Usuario"), "ClavePrueba123".toCharArray());
            } catch (SQLException ex) {
                duplicado = ex.getErrorCode() == 1;
            }
            comprobar(duplicado, "Usuario duplicado rechazado");
            usuarios.actualizar(new Usuario(sesion.getId(), marca, "Actualizado", "Temporal"), new char[0]);
            comprobar(usuarios.autenticar(marca, "ClavePrueba123".toCharArray()).getNombre().equals("Actualizado"), "Conservar clave al editar");
            usuarios.actualizar(sesion, "NuevaClave123".toCharArray());
            comprobar(usuarios.autenticar(marca, "ClavePrueba123".toCharArray()) == null, "Invalidar clave anterior");
            comprobar(usuarios.autenticar(marca, "NuevaClave123".toCharArray()) != null, "Nueva clave válida");
            boolean protegido = false;
            try {
                usuarios.eliminar(sesion.getId(), sesion.getId());
            } catch (IllegalArgumentException ex) {
                protegido = true;
            }
            comprobar(protegido, "Proteger cuenta activa");

            categorias.insertar(new Categoria(null, marca, "Temporal"));
            Categoria categoria = categorias.listar().get(0);
            comprobar(categoria.getNombre().equals(marca), "Categoría reciente primero");
            categorias.actualizar(new Categoria(categoria.getId(), marca, "Actualizada"));
            comprobar(categorias.listar().get(0).getDescripcion().equals("Actualizada"), "Actualizar categoría");
            proveedores.insertar(new Proveedor(null, marca, "Empresa", marca, "", ""));
            Proveedor proveedor = proveedores.listar().get(0);
            comprobar(proveedor.getNit().equals(marca), "Proveedor reciente primero");
            proveedores.actualizar(new Proveedor(proveedor.getId(), marca, "Actualizada", marca, "1234", "prueba@example.test"));
            comprobar(proveedores.listar().get(0).getEmpresa().equals("Actualizada"), "Actualizar proveedor");
            Producto producto = new Producto(null, codigo, "Temporal", new BigDecimal("10.50"), 2, categoria, proveedor, true);
            productos.insertar(producto);
            Producto guardado = productos.listar().get(0);
            comprobar(guardado.getCodigo().equals(codigo), "Producto reciente primero");
            boolean referencia = false;
            try {
                categorias.eliminar(categoria.getId());
            } catch (SQLException ex) {
                referencia = ex.getErrorCode() == 2292;
            }
            comprobar(referencia, "Proteger categoría utilizada");
            referencia = false;
            try {
                proveedores.eliminar(proveedor.getId());
            } catch (SQLException ex) {
                referencia = ex.getErrorCode() == 2292;
            }
            comprobar(referencia, "Proteger proveedor utilizado");
            duplicado = false;
            try {
                productos.insertar(producto);
            } catch (SQLException ex) {
                duplicado = ex.getErrorCode() == 1;
            }
            comprobar(duplicado, "Código duplicado rechazado");
            productos.actualizar(new Producto(guardado.getId(), codigo, "Actualizado", BigDecimal.ZERO, 0, categoria, proveedor, false));
            comprobar(!productos.listar().get(0).isActivo(), "Actualizar producto");
            productos.eliminar(guardado.getId());
            categorias.eliminar(categoria.getId());
            proveedores.eliminar(proveedor.getId());
            usuarios.eliminar(sesion.getId(), -1);
            comprobar(usuarios.autenticar(marca, "NuevaClave123".toCharArray()) == null, "Eliminar usuario");
            System.out.println("OK: login, CRUD, orden y restricciones en Oracle.");
        } finally {
            limpiar("DELETE FROM productos WHERE codigo = ?", codigo);
            limpiar("DELETE FROM categorias WHERE nombre = ?", marca);
            limpiar("DELETE FROM proveedores WHERE nit = ?", marca);
            limpiar("DELETE FROM usuarios WHERE usuario = ?", marca);
        }
    }

    private static void limpiar(String sql, String marca) throws SQLException {
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, marca);
            consulta.executeUpdate();
        }
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
