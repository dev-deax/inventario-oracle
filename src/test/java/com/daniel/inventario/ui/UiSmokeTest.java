package com.daniel.inventario.ui;

import com.daniel.inventario.model.Categoria;
import com.daniel.inventario.model.Usuario;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class UiSmokeTest {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    verificar();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        System.out.println("OK: formularios Swing, selección, limpieza y capturas sin conexión a Oracle.");
    }

    private static void verificar() throws Exception {
        Style.aplicar();
        UsuarioPanel usuarios = new UsuarioPanel(new Usuario(1L, "demo", "Ana", "Pérez"));
        comprobar(usuarios.modelo.getColumnCount() == 4, "La tabla de usuarios no debe exponer contraseñas");
        CategoriaPanel categorias = new CategoriaPanel();
        ArrayList<Categoria> datos = new ArrayList<>();
        datos.add(new Categoria(10L, "Tecnología", "Accesorios"));
        Field lista = CategoriaPanel.class.getDeclaredField("datos");
        lista.setAccessible(true);
        lista.set(categorias, datos);
        categorias.modelo.addRow(new Object[]{10L, "Tecnología", "Accesorios"});
        categorias.tabla.setRowSelectionInterval(0, 0);
        comprobar(categorias.idSeleccionado.equals(10L), "Selección de fila");
        Field nombre = CategoriaPanel.class.getDeclaredField("txtNombre");
        nombre.setAccessible(true);
        comprobar(((JTextField) nombre.get(categorias)).getText().equals("Tecnología"), "Cargar datos en formulario");
        comprobar(!categorias.modelo.isCellEditable(0, 0), "Tabla de solo lectura");
        capturar(categorias, "categorias");
        categorias.limpiar();
        comprobar(categorias.idSeleccionado == null && categorias.tabla.getSelectedRow() == -1, "Limpiar selección");
        capturar(usuarios, "usuarios");
        capturar(new ProveedorPanel(), "proveedores");
        capturar(new ProductoPanel(), "productos");
        capturar(new DashboardPanel("Ana"), "resumen");
    }

    private static void capturar(JPanel panel, String nombre) throws Exception {
        panel.setSize(1075, 740);
        distribuir(panel);
        BufferedImage imagen = new BufferedImage(1075, 740, BufferedImage.TYPE_INT_RGB);
        Graphics2D dibujo = imagen.createGraphics();
        panel.printAll(dibujo);
        dibujo.dispose();
        File carpeta = new File("target/capturas");
        carpeta.mkdirs();
        ImageIO.write(imagen, "png", new File(carpeta, nombre + ".png"));
    }

    private static void distribuir(Container contenedor) {
        contenedor.doLayout();
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof Container) {
                distribuir((Container) componente);
            }
        }
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
