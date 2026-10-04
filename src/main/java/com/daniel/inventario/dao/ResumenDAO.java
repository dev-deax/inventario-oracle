package com.daniel.inventario.dao;

import com.daniel.inventario.db.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResumenDAO {
    public int[] obtenerCantidades() throws SQLException {
        String sql = "SELECT (SELECT COUNT(*) FROM usuarios) usuarios, "
                + "(SELECT COUNT(*) FROM categorias) categorias, "
                + "(SELECT COUNT(*) FROM proveedores) proveedores, "
                + "(SELECT COUNT(*) FROM productos) productos FROM dual";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql);
                ResultSet resultado = consulta.executeQuery()) {
            resultado.next();
            return new int[]{resultado.getInt("usuarios"), resultado.getInt("categorias"),
                resultado.getInt("proveedores"), resultado.getInt("productos")};
        }
    }
}
