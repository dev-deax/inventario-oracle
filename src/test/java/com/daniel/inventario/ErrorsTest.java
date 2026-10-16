package com.daniel.inventario;

import com.daniel.inventario.util.Dialogs;
import com.daniel.inventario.util.Validators;
import java.sql.SQLException;

public class ErrorsTest {
    public static void main(String[] args) {
        String mensaje = Dialogs.mensajeError(new SQLException("ORA-99999: detalle interno", "99999", 99999));
        comprobar(!mensaje.contains("ORA-") && !mensaje.contains("detalle interno"), "Ocultar SQL desconocido");
        comprobar(Dialogs.mensajeError(new SQLException("interno", "", 1)).contains("Ya existe"), "Duplicados");
        comprobar(Dialogs.mensajeError(new SQLException("interno", "", 2292)).contains("No se puede eliminar"), "Claves foráneas");
        comprobar(Dialogs.mensajeError(new IllegalArgumentException("Selecciona un registro."))
                .equals("Selecciona un registro."), "Conservar validación amigable");
        comprobar(Validators.telefono("+502 (5555)-1234").equals("+502 (5555)-1234"), "Teléfono válido");
        comprobar(Validators.telefono("").isEmpty(), "Teléfono opcional");
        comprobar(Validators.texto(null, "Descripción", 250).isEmpty(), "Texto opcional nulo");
        String[] telefonos = {"abc", "++", "5555@1234"};
        for (String valor : telefonos) {
            boolean rechazado = false;
            try {
                Validators.telefono(valor);
            } catch (IllegalArgumentException ex) {
                rechazado = true;
            }
            comprobar(rechazado, "Teléfono inválido");
        }
        boolean rechazado = false;
        try {
            Validators.correo("correo-sin-arroba");
        } catch (IllegalArgumentException ex) {
            rechazado = true;
        }
        comprobar(rechazado, "Formato de correo");
        System.out.println("OK: mensajes amigables y formatos.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
