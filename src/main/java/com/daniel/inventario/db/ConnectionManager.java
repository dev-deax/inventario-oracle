package com.daniel.inventario.db;

import com.daniel.inventario.config.DatabaseConfig;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    public static Connection conectar() throws SQLException {
        try {
            Properties datos = DatabaseConfig.cargar();
            Properties conexion = new Properties();
            conexion.setProperty("user", datos.getProperty("db.user"));
            conexion.setProperty("password", datos.getProperty("db.password"));
            conexion.setProperty("oracle.net.CONNECT_TIMEOUT", "5000");
            conexion.setProperty("oracle.jdbc.ReadTimeout", "10000");
            return DriverManager.getConnection(datos.getProperty("db.url"), conexion);
        } catch (IOException ex) {
            throw new SQLException(ex.getMessage(), ex);
        }
    }
}
