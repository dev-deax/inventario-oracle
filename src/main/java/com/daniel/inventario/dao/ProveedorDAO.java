package com.daniel.inventario.dao;

import com.daniel.inventario.db.ConnectionManager;
import com.daniel.inventario.model.Proveedor;
import com.daniel.inventario.util.Validators;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProveedorDAO {
    public ArrayList<Proveedor> listar() throws SQLException {
        return buscar("");
    }

    public ArrayList<Proveedor> buscar(String texto) throws SQLException {
        ArrayList<Proveedor> datos = new ArrayList<>();
        String sql = "SELECT id, nombre, empresa, nit, telefono, email FROM proveedores"
                + "WHERE (? IS NULL OR INSTR(LOWER(TO_CHAR(id) || ' ' || nombre || ' ' || empresa || ' ' || nit || ' ' || telefono || ' ' || email), LOWER(?)) > 0)"
                + " ORDER BY creado_en DESC, id DESC";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            String filtro = Validators.texto(texto, "Búsqueda", 150);
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    datos.add(new Proveedor(resultado.getLong("id"), resultado.getString("nombre"), resultado.getString("empresa"), resultado.getString("nit"), resultado.getString("telefono"), resultado.getString("email")));
                }
            }
        }
        return datos;
    }

    public void insertar(Proveedor dato) throws SQLException {
        String sql = "INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email) VALUES (proveedores_seq.NEXTVAL, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignar(consulta, dato);
            consulta.executeUpdate();
        }
    }

    public void actualizar(Proveedor dato) throws SQLException {
        String sql = "UPDATE proveedores SET nombre = ?, empresa = ?, nit = ?, telefono = ?, email = ? WHERE id = ?";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignar(consulta, dato);
            consulta.setLong(6, dato.getId());
            if (consulta.executeUpdate() == 0) {
                throw new IllegalArgumentException("El registro ya no existe. Actualiza la lista.");
            }
        }
    }

    private void asignar(PreparedStatement consulta, Proveedor dato) throws SQLException {
        consulta.setString(1, Validators.requerido(dato.getNombre(), "Nombre", 120));
        consulta.setString(2, Validators.requerido(dato.getEmpresa(), "Empresa", 150));
        consulta.setString(3, Validators.requerido(dato.getNit(), "NIT", 30));
        consulta.setString(4, Validators.telefono(dato.getTelefono()));
        consulta.setString(5, Validators.correo(dato.getEmail()));
    }

    public void eliminar(long id) throws SQLException {
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement("DELETE FROM proveedores WHERE id = ?")) {
            consulta.setLong(1, id);
            if (consulta.executeUpdate() == 0) {
                throw new IllegalArgumentException("El registro ya no existe. Actualiza la lista.");
            }
        }
    }
}
