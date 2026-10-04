package com.daniel.inventario;

import com.daniel.inventario.util.Passwords;
import com.daniel.inventario.util.Validators;

public class PasswordsTest {
    public static void main(String[] args) throws Exception {
        char[] clave = "PruebaSegura123".toCharArray();
        String primero = Passwords.generar(clave);
        String segundo = Passwords.generar(clave);
        comprobar(!primero.equals(segundo), "Cada contraseña debe tener una sal diferente");
        comprobar(Passwords.verificar(clave, primero), "La clave correcta debe funcionar");
        comprobar(!Passwords.verificar("OtraClave123".toCharArray(), primero), "La clave incorrecta debe fallar");
        comprobar(!Passwords.verificar(clave, "hash-invalido"), "El hash inválido debe rechazarse");
        comprobar(Validators.usuario("  Daniel  ").equals("daniel"), "Normalización de usuario");
        boolean rechazada = false;
        try {
            Passwords.generar("corta".toCharArray());
        } catch (IllegalArgumentException ex) {
            rechazada = true;
        }
        comprobar(rechazada, "No aceptar contraseñas cortas");
        System.out.println("OK: contraseñas y normalización de usuario.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
