package com.daniel.inventario.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LoginLayoutTest {
    public static void main(String[] args) throws Exception {
        final int tamano = Integer.parseInt(args[0]);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                Style.aplicar();
                UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, tamano));
                UIManager.put("PasswordField.font", new Font("SansSerif", Font.PLAIN, tamano));
                LoginFrame ventana = new LoginFrame();
                try {
                    ventana.validate();
                    revisar(ventana.getContentPane());
                    int escala = Integer.parseInt(System.getProperty("sun.java2d.uiScale", "1"));
                    Container contenido = ventana.getContentPane();
                    BufferedImage imagen = new BufferedImage(contenido.getWidth() * escala,
                            contenido.getHeight() * escala, BufferedImage.TYPE_INT_RGB);
                    Graphics2D dibujo = imagen.createGraphics();
                    dibujo.scale(escala, escala);
                    contenido.printAll(dibujo);
                    dibujo.dispose();
                    File carpeta = new File("target/capturas");
                    carpeta.mkdirs();
                    ImageIO.write(imagen, "png", new File(carpeta,
                            "login-" + escala + "x-fuente-" + tamano + ".png"));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    ventana.dispose();
                }
            }
        });
        System.out.println("OK: login sin recortes, fuente " + tamano);
    }

    private static void revisar(Container contenedor) {
        contenedor.doLayout();
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof JTextField) {
                JTextField campo = (JTextField) componente;
                campo.setText("Prueba_Ágjpq123");
                Insets margen = campo.getInsets();
                int necesario = campo.getFontMetrics(campo.getFont()).getHeight()
                        + margen.top + margen.bottom;
                if (campo.getHeight() < necesario) {
                    throw new AssertionError("Campo recortado: " + campo.getHeight() + " < " + necesario);
                }
                if (campo instanceof JPasswordField && ((JPasswordField) campo).getEchoChar() == 0) {
                    throw new AssertionError("La contraseña debe permanecer oculta");
                }
            }
            if (componente instanceof Container) {
                revisar((Container) componente);
            }
        }
    }
}