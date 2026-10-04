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

public class UsuarioDAO {
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
