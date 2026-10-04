package com.daniel.inventario.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DatabaseConfig {
    public static Properties cargar() throws IOException {
        Path archivo = Path.of("db.properties");
        if (!Files.exists(archivo)) {
            throw new IOException("Copia db.properties.example como db.properties y configura Oracle.");
        }
        Properties datos = new Properties();
        try (InputStream entrada = Files.newInputStream(archivo)) {
            datos.load(entrada);
        }
        String[] campos = {"db.url", "db.user", "db.password"};
        for (String campo : campos) {
            String valor = datos.getProperty(campo);
            if (valor == null || valor.isBlank()) {
                throw new IOException("Falta configurar " + campo + " en db.properties.");
            }
        }
        String usuario = datos.getProperty("db.user");
        if (usuario.equalsIgnoreCase("SYSTEM") || usuario.equalsIgnoreCase("SYS")) {
            throw new IOException("Utiliza INVENTARIO_APP, no una cuenta administradora de Oracle.");
        }
        return datos;
    }
}
