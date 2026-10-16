package com.daniel.inventario.dao;

import com.daniel.inventario.db.ConnectionManager;
import com.daniel.inventario.model.Usuario;
import com.daniel.inventario.util.Passwords;
import com.daniel.inventario.util.Validators;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO {
    public ArrayList<Usuario> listar() throws SQLException {
        return buscar("");
    }

    public ArrayList<Usuario> buscar(String texto) throws SQLException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, usuario, nombre, apellido FROM usuarios"
                + "WHERE (? IS NULL OR INSTR(LOWER(TO_CHAR(id) || ' ' || usuario || ' ' || nombre || ' ' || apellido), LOWER(?)) > 0)"
                + " ORDER BY creado_en DESC, id DESC";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            String filtro = Validators.texto(texto, "Búsqueda", 150);
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    usuarios.add(new Usuario(resultado.getLong("id"), resultado.getString("usuario"),
                            resultado.getString("nombre"), resultado.getString("apellido")));
                }
            }
        }
        return usuarios;
    }

    public void actualizar(Usuario usuario, char[] password) throws SQLException, GeneralSecurityException {
        String sql = "UPDATE usuarios SET usuario = ?, nombre = ?, apellido = ?";
        boolean cambiarPassword = password.length > 0;
        String hash = null;
        if (cambiarPassword) {
            hash = Passwords.generar(password);
            sql += ", password_hash = ?";
        }
        sql += " WHERE id = ?";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, Validators.usuario(usuario.getUsuario()));
            consulta.setString(2, Validators.requerido(usuario.getNombre(), "Nombre", 80));
            consulta.setString(3, Validators.requerido(usuario.getApellido(), "Apellido", 80));
            if (cambiarPassword) {
                consulta.setString(4, hash);
                consulta.setLong(5, usuario.getId());
            } else {
                consulta.setLong(4, usuario.getId());
            }
            if (consulta.executeUpdate() == 0) {
                throw new SQLException("El usuario ya no existe. Actualiza la lista.");
            }
        }
    }

    public void eliminar(long id, long idSesion) throws SQLException {
        if (id == idSesion) {
            throw new IllegalArgumentException("No puedes eliminar la cuenta de tu sesión actual.");
        }
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            consulta.setLong(1, id);
            if (consulta.executeUpdate() == 0) {
                throw new SQLException("El usuario ya no existe. Actualiza la lista.");
            }
        }
    }

    public Usuario autenticar(String nombreUsuario, char[] password)
            throws SQLException, GeneralSecurityException {
        String usuario = Validators.usuario(nombreUsuario);
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException("Ingresa tu contraseña.");
        }
        String sql = "SELECT id, usuario, nombre, apellido, password_hash FROM usuarios WHERE usuario = ?";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, usuario);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next() && Passwords.verificar(password, resultado.getString("password_hash"))) {
                    return new Usuario(resultado.getLong("id"), resultado.getString("usuario"),
                            resultado.getString("nombre"), resultado.getString("apellido"));
                }
            }
        }
        return null;
    }

    public void insertar(Usuario usuario, char[] password) throws SQLException, GeneralSecurityException {
        String hash = Passwords.generar(password);
        String sql = "INSERT INTO usuarios (id, usuario, nombre, apellido, password_hash) "
                + "VALUES (usuarios_seq.NEXTVAL, ?, ?, ?, ?)";
        try (Connection conexion = ConnectionManager.conectar();
                PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, Validators.usuario(usuario.getUsuario()));
            consulta.setString(2, Validators.requerido(usuario.getNombre(), "Nombre", 80));
            consulta.setString(3, Validators.requerido(usuario.getApellido(), "Apellido", 80));
            consulta.setString(4, hash);
            consulta.executeUpdate();
        }
    }
}
