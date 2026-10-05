package com.daniel.inventario.util;

import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Dialogs {
    public static void error(Component ventana, Exception error) {
        String mensaje = error.getMessage();
        if (error instanceof SQLException) {
            SQLException sql = (SQLException) error;
            if (sql.getErrorCode() == 1) {
                mensaje = "Ya existe un registro con ese usuario, nombre, código o NIT.";
            } else if (sql.getErrorCode() == 2292) {
                mensaje = "No se puede eliminar: otro registro utiliza estos datos.";
            } else if (sql.getErrorCode() == 2291) {
                mensaje = "La categoría o el proveedor ya no existe. Actualiza la lista.";
            } else if (sql.getErrorCode() == 1017 || sql.getErrorCode() == 28000) {
                mensaje = "Revisa la cuenta de Oracle configurada en config/database.properties.";
            } else if (sql.getErrorCode() == 942) {
                mensaje = "Faltan tablas. Ejecuta los scripts del proyecto como INVENTARIO_CURSO.";
            } else if (sql.getSQLState() != null && sql.getSQLState().startsWith("08")
                    || sql.getErrorCode() == 17002 || sql.getErrorCode() == 12514
                    || sql.getErrorCode() == 12541) {
                mensaje = "No fue posible conectar con Oracle. Revisa el servicio y config/database.properties.";
            }
        }
        JOptionPane.showMessageDialog(ventana, mensaje, "No se pudo completar", JOptionPane.ERROR_MESSAGE);
    }

    public static void exito(Component ventana, String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Listo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component ventana, String mensaje) {
        return JOptionPane.showConfirmDialog(ventana, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
