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
    private final Usuario sesion;

    public MainFrame(Usuario sesion) {
        super("Inventario Oracle");
        if (sesion == null || sesion.getId() == null) {
            throw new IllegalArgumentException("Debes iniciar sesión.");
        }
        this.sesion = sesion;
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

        JPanel inicio = new JPanel(new BorderLayout());
        inicio.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        inicio.add(UiFactory.titulo("Bienvenido, " + sesion.getNombre()), BorderLayout.NORTH);
        agregarPantalla("Inicio", inicio);
        agregarPantalla("Usuarios", new UsuarioPanel(sesion));
        agregarPantalla("Categorías", new CategoriaPanel());
        agregarPantalla("Proveedores", new ProveedorPanel());
        menu.add(Box.createVerticalGlue());
        JButton salir = UiFactory.boton("Cerrar sesión", Style.SUAVE);
        salir.setActionCommand("salir");
        salir.addActionListener(this);
        menu.add(salir);
        add(menu, BorderLayout.WEST);
        add(pantallas, BorderLayout.CENTER);
    }

    private void agregarPantalla(String nombre, JPanel panel) {
        pantallas.add(panel, nombre);
        JButton boton = UiFactory.boton(nombre, Style.AZUL);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 42));
        boton.setActionCommand(nombre);
        boton.addActionListener(this);
        menu.add(boton);
        menu.add(Box.createVerticalStrut(8));
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
        distribucion.show(pantallas, accion);
        for (Component panel : pantallas.getComponents()) {
            if (panel.isVisible() && panel instanceof Refreshable) {
                ((Refreshable) panel).cargarDatos();
            }
        }
    }
}
