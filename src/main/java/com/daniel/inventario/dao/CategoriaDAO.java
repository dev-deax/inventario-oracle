package com.daniel.inventario.dao;

import com.daniel.inventario.db.ConnectionManager;
import com.daniel.inventario.model.Categoria;
import com.daniel.inventario.util.Validators;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaDAO {
    public ArrayList<Categoria> listar() throws SQLException {
        return buscar("");
    }

    public ArrayList<Categoria> buscar(String texto) throws SQLException {
        ArrayList<Categoria> datos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, creado_en FROM categorias"
                + " WHERE (? IS NULL OR INSTR(LOWER(TO_CHAR(id) || ' ' || nombre || ' ' || descripcion), LOWER(?)) > 0)"
                + " ORDER BY creado_en DESC, id DESC";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            String filtro = Validators.texto(texto, "Búsqueda", 150);
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    datos.add(new Categoria(resultado.getLong("id"), resultado.getString("nombre"), resultado.getString("descripcion"), resultado.getTimestamp("creado_en").toLocalDateTime()));
                }
            }
        }
        return datos;
    }

    public void insertar(Categoria dato) throws SQLException {
        String sql = "INSERT INTO categorias (id, nombre, descripcion) VALUES (categorias_seq.NEXTVAL, ?, ?)";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignar(consulta, dato);
            consulta.executeUpdate();
        }
    }

    public void actualizar(Categoria dato) throws SQLException {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            asignar(consulta, dato);
            consulta.setLong(3, dato.getId());
            if (consulta.executeUpdate() == 0) {
                throw new IllegalArgumentException("El registro ya no existe. Actualiza la lista.");
            }
        }
    }

    private void asignar(PreparedStatement consulta, Categoria dato) throws SQLException {
        consulta.setString(1, Validators.requerido(dato.getNombre(), "Nombre", 100));
        consulta.setString(2, Validators.texto(dato.getDescripcion(), "Descripción", 250));
    }

    public void eliminar(long id) throws SQLException {
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement("DELETE FROM categorias WHERE id = ?")) {
            consulta.setLong(1, id);
            if (consulta.executeUpdate() == 0) {
                throw new IllegalArgumentException("El registro ya no existe. Actualiza la lista.");
            }
        }
    }
}
