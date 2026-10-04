package com.daniel.inventario.db;

import com.daniel.inventario.dao.UsuarioDAO;
import com.daniel.inventario.model.Usuario;
import java.io.Console;
import java.util.Arrays;

public class CrearUsuarioInicial {
    public static void main(String[] args) throws Exception {
        Console consola = System.console();
        if (consola == null) {
            System.out.println("Ejecuta esta utilidad desde una terminal con java, no desde la consola de NetBeans.");
            return;
        }
        String usuario = consola.readLine("Usuario: ");
        String nombre = consola.readLine("Nombre: ");
        String apellido = consola.readLine("Apellido: ");
        char[] password = consola.readPassword("Contraseña (mínimo 8 caracteres): ");
        if (password == null) {
            return;
        }
        try {
            new UsuarioDAO().insertar(new Usuario(null, usuario, nombre, apellido), password);
            System.out.println("Cuenta creada correctamente.");
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
