package com.daniel.inventario.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Passwords {
    private static final int ITERACIONES = 600000;

    public static String generar(char[] password) throws GeneralSecurityException {
        if (password == null || password.length < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        byte[] sal = new byte[16];
        new SecureRandom().nextBytes(sal);
        byte[] hash = calcular(password, sal, ITERACIONES);
        return ITERACIONES + ":" + Base64.getEncoder().encodeToString(sal)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(char[] password, String guardado) throws GeneralSecurityException {
        if (password == null || guardado == null) {
            return false;
        }
        try {
            String[] partes = guardado.split(":");
            if (partes.length != 3) {
                return false;
            }
            int iteraciones = Integer.parseInt(partes[0]);
            if (iteraciones < 600000 || iteraciones > 2000000) {
                return false;
            }
            byte[] sal = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            if (sal.length != 16 || esperado.length != 32) {
                return false;
            }
            return MessageDigest.isEqual(esperado, calcular(password, sal, iteraciones));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static byte[] calcular(char[] password, byte[] sal, int iteraciones)
            throws GeneralSecurityException {
        PBEKeySpec datos = new PBEKeySpec(password, sal, iteraciones, 256);
        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return fabrica.generateSecret(datos).getEncoded();
        } finally {
            datos.clearPassword();
        }
    }
}
