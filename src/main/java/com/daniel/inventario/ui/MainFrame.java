package com.daniel.inventario.ui;

import com.daniel.inventario.model.Usuario;
import com.daniel.inventario.util.Dialogs;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener {
    private final CardLayout distribucion = new CardLayout();
    private final JPanel pantallas = new JPanel(distribucion);
    private final JPanel menu = new JPanel();

    public MainFrame(Usuario sesion) {
        super("Inventario Oracle");
        if (sesion == null || sesion.getId() == null) {
            throw new IllegalArgumentException("Debes iniciar sesión.");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(1050, 700));
        setSize(1280, 780);
        setLocationRelativeTo(null);

        // El menú permanece fijo; CardLayout cambia el panel del centro.
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(24, 16, 20, 16));
        menu.setBackground(Style.TEXTO);
        menu.setPreferredSize(new Dimension(205, 0));
        JLabel marca = new JLabel("INVENTARIO");
        marca.setForeground(Style.PANEL);
        marca.setFont(Style.NEGRITA);
        menu.add(marca);
        menu.add(Box.createVerticalStrut(25));

        DashboardPanel inicio = new DashboardPanel(sesion.getNombre());
        agregarPantalla("Inicio", inicio);
        agregarPantalla("Usuarios", new UsuarioPanel(sesion));
        agregarPantalla("Categorías", new CategoriaPanel());
        agregarPantalla("Proveedores", new ProveedorPanel());
        agregarPantalla("Productos", new ProductoPanel());
        menu.add(Box.createVerticalGlue());
        JButton salir = UiFactory.botonMenu("Cerrar sesión");
        salir.setActionCommand("salir");
        salir.addActionListener(this);
        menu.add(salir);
        add(menu, BorderLayout.WEST);
        add(pantallas, BorderLayout.CENTER);
        marcarSeccion("Inicio");
        inicio.cargarDatos();
    }

    private void agregarPantalla(String nombre, JPanel panel) {
        pantallas.add(panel, nombre);
        JButton boton = UiFactory.botonMenu(nombre);


        boton.setActionCommand(nombre);
        boton.addActionListener(this);
        menu.add(boton);
        menu.add(Box.createVerticalStrut(8));
    }

    private void marcarSeccion(String nombre) {
        for (Component componente : menu.getComponents()) {
            if (componente instanceof JButton) {
                JButton boton = (JButton) componente;
                boolean activo = boton.getActionCommand().equals(nombre);
                if (activo) {
                    boton.setBackground(Style.AZUL);
                    boton.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 4, 0, 0, Style.PANEL),
                            BorderFactory.createEmptyBorder(8, 10, 8, 14)));
                    boton.getAccessibleContext().setAccessibleDescription("Sección actual");
                } else {
                    boton.setBackground(Style.SUAVE);
                    boton.setBorder(javax.swing.UIManager.getBorder("Button.border"));
                    boton.getAccessibleContext().setAccessibleDescription("Abrir " + boton.getText());
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        if (accion.equals("salir")) {
            if (Dialogs.confirmar(this, "¿Deseas cerrar la sesión?")) {
                new LoginFrame().setVisible(true);
                dispose();
            }
            return;
        }
        marcarSeccion(accion);
        distribucion.show(pantallas, accion);
        for (Component panel : pantallas.getComponents()) {
            if (panel.isVisible() && panel instanceof Refreshable) {
                ((Refreshable) panel).cargarDatos();
            }
        }
    }
}
