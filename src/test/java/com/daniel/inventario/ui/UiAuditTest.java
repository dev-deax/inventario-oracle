package com.daniel.inventario.ui;

import com.daniel.inventario.model.Usuario;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;

import javax.swing.SwingUtilities;

public class UiAuditTest {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                Style.aplicar();
                MainFrame ventana = new MainFrame(new Usuario(1L, "demo", "Daniel", "Demo"));
                try {
                    ventana.addNotify();
                    String[] pantallas = {"Inicio", "Usuarios", "Categorías", "Proveedores", "Productos"};
                    int[] anchos = {1050, 1280};
                    for (int ancho : anchos) {
                        ventana.setSize(ancho, ancho == 1050 ? 700 : 780);
                        for (String pantalla : pantallas) {
                            ventana.actionPerformed(new ActionEvent(ventana, 0, pantalla));
                            ventana.validate();
                            distribuir(ventana.getContentPane());
                            revisarMenu((Container) ventana.getContentPane().getComponent(0));
                            revisarAcciones(ventana.getContentPane());
                            revisarFechas(ventana.getContentPane());
                            capturar(ventana.getContentPane(), pantalla, ancho);
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    ventana.dispose();
                }
            }
        });
        System.out.println("OK: cinco pantallas, botones del menú iguales y acciones alineadas.");
    }

    private static void revisarMenu(Container menu) {
        Dimension referencia = null;
        for (Component componente : menu.getComponents()) {
            if (componente instanceof JButton) {
                if (referencia == null) {
                    referencia = componente.getSize();
                }
                if (!referencia.equals(componente.getSize())) {
                    throw new AssertionError("Tamaño diferente: " + ((JButton) componente).getText());
                }
            }
        }
    }

    private static void revisarAcciones(Container contenedor) {
        if (!contenedor.isVisible()) {
            return;
        }
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof JButton) {
                JButton boton = (JButton) componente;
                if (boton.getText().equals("Actualizar") && !boton.getSize().equals(new Dimension(130, 40))) {
                    throw new AssertionError("Dimensiones incorrectas del botón Actualizar");
                }
                if (boton.getText().equals("Nuevo") || (boton.getText().equals("Actualizar")
                        && boton.getParent().getComponent(0) == boton)) {
                    if (boton.getX() != 0) {
                        throw new AssertionError("Margen extra en acciones");
                    }
                }
            }
            if (componente instanceof Container) {
                revisarAcciones((Container) componente);
            }
        }
    }

    private static void revisarFechas(Container contenedor) {
        if (!contenedor.isVisible()) {
            return;
        }
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof javax.swing.JTable) {
                javax.swing.JTable tabla = (javax.swing.JTable) componente;
                int columna = tabla.getColumnCount() - 1;
                if (!tabla.getColumnName(columna).equals("Fecha de creación")) {
                    throw new AssertionError("Falta la columna de fecha");
                }
                for (int fila = 0; fila < tabla.getRowCount(); fila++) {
                    String fecha = String.valueOf(tabla.getValueAt(fila, columna));
                    if (!fecha.matches("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}")) {
                        throw new AssertionError("Formato de fecha incorrecto: " + fecha);
                    }
                }
            }
            if (componente instanceof Container) {
                revisarFechas((Container) componente);
            }
        }
    }

    private static void distribuir(Container contenedor) {
        contenedor.doLayout();
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof Container) {
                distribuir((Container) componente);
            }
        }
    }

    private static void capturar(Container contenido, String nombre, int ancho) throws Exception {
        int escala = Integer.parseInt(System.getProperty("sun.java2d.uiScale", "1"));
        BufferedImage imagen = new BufferedImage(contenido.getWidth() * escala,
                contenido.getHeight() * escala, BufferedImage.TYPE_INT_RGB);
        Graphics2D dibujo = imagen.createGraphics();
        dibujo.scale(escala, escala);
        contenido.printAll(dibujo);
        dibujo.dispose();
        File carpeta = new File("target/capturas");
        carpeta.mkdirs();
        ImageIO.write(imagen, "png", new File(carpeta,
                "auditoria-" + nombre + "-" + ancho + "-" + escala + "x.png"));
    }
}
