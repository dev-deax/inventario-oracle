package com.daniel.inventario.util;

import java.math.BigDecimal;
import java.util.Locale;

public class Validators {
    public static String requerido(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        return texto(valor, campo, maximo);
    }

    public static String texto(String valor, String campo, int maximo) {
        String resultado = valor == null ? "" : valor.trim();
        if (resultado.length() > maximo) {
            throw new IllegalArgumentException(campo + " admite hasta " + maximo + " caracteres.");
        }
        return resultado;
    }

    public static String usuario(String valor) {
        String resultado = requerido(valor, "Usuario", 40).toLowerCase(Locale.ROOT);
        if (!resultado.matches("[a-z0-9._-]+")) {
            throw new IllegalArgumentException("El usuario admite letras sin tildes, números, punto, guion y guion bajo.");
        }
        return resultado;
    }

    public static String correo(String valor) {
        String resultado = texto(valor, "Correo", 150);
        if (!resultado.isEmpty() && !resultado.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        return resultado;
    }

    public static String telefono(String valor) {
        String resultado = texto(valor, "Teléfono", 30);
        if (!resultado.isEmpty() && (!resultado.matches("[+0-9() .-]+")
                || !resultado.matches(".*[0-9].*"))) {
            throw new IllegalArgumentException("El teléfono solo admite números, espacios, +, guiones y paréntesis.");
        }
        return resultado;
    }

    public static BigDecimal precio(String valor) {
        try {
            String texto = requerido(valor, "Precio", 20);
            if (!texto.matches("[0-9]+(\\.[0-9]+)?")) {
                throw new IllegalArgumentException("Ingresa un precio válido, por ejemplo 25.50.");
            }
            BigDecimal numero = new BigDecimal(texto);
            if (numero.signum() < 0 || numero.compareTo(new BigDecimal("9999999999.99")) > 0
                    || numero.stripTrailingZeros().scale() > 2) {
                throw new IllegalArgumentException("El precio debe ser positivo o cero, con máximo 2 decimales y 10 enteros.");
            }
            return numero;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ingresa un precio válido, por ejemplo 25.50.");
        }
    }

    public static int stock(String valor) {
        try {
            int numero = Integer.parseInt(requerido(valor, "Stock", 10));
            if (numero < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo.");
            }
            return numero;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El stock debe ser un entero entre 0 y 2147483647.");
        }
    }
}
