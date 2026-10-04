package com.daniel.inventario;

import com.daniel.inventario.util.Validators;

public class ValidatorsTest {
    public static void main(String[] args) {
        String[] preciosInvalidos = {"-1", "texto", "", "1.234", "10000000000"};
        for (String precio : preciosInvalidos) {
            boolean rechazado = false;
            try {
                Validators.precio(precio);
            } catch (IllegalArgumentException ex) {
                rechazado = true;
            }
            comprobar(rechazado, "Precio inválido: " + precio);
        }
        String[] stockInvalido = {"-1", "1.2", "2147483648", ""};
        for (String stock : stockInvalido) {
            boolean rechazado = false;
            try {
                Validators.stock(stock);
            } catch (IllegalArgumentException ex) {
                rechazado = true;
            }
            comprobar(rechazado, "Stock inválido: " + stock);
        }
        comprobar(Validators.precio("0").signum() == 0, "Precio cero permitido");
        comprobar(Validators.stock("0") == 0, "Stock cero permitido");
        comprobar(Validators.correo("").isEmpty(), "Correo opcional");
        comprobar(Validators.requerido(" Ana ", "Nombre", 80).equals("Ana"), "Recortar espacios");
        boolean rechazado = false;
        try {
            Validators.requerido(" ", "Nombre", 80);
        } catch (IllegalArgumentException ex) {
            rechazado = true;
        }
        comprobar(rechazado, "Nombre obligatorio");
        System.out.println("OK: validaciones de campos, precio y stock.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
