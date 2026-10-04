package com.daniel.inventario.ui;

import com.daniel.inventario.dao.ResumenDAO;
import com.daniel.inventario.util.Dialogs;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DashboardPanel extends JPanel implements Refreshable, ActionListener {
    private final JLabel[] valores = new JLabel[4];

    public DashboardPanel(String nombre) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        add(UiFactory.titulo("Bienvenido, " + nombre), BorderLayout.NORTH);
        String[] titulos = {"Usuarios", "Categorías", "Proveedores", "Productos"};
        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 18, 18));
        for (int i = 0; i < titulos.length; i++) {
            JPanel tarjeta = new JPanel(new BorderLayout(0, 16));
            tarjeta.setBackground(Style.PANEL);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Style.BORDE),
                    BorderFactory.createEmptyBorder(24, 24, 24, 24)));
            JLabel titulo = new JLabel(titulos[i]);
            titulo.setFont(Style.NEGRITA);
            valores[i] = new JLabel("—");
            valores[i].setFont(Style.TITULO);
            valores[i].setForeground(Style.AZUL);
            tarjeta.add(titulo, BorderLayout.NORTH);
            tarjeta.add(valores[i], BorderLayout.CENTER);
            tarjetas.add(tarjeta);
        }
        add(tarjetas, BorderLayout.CENTER);
        JButton actualizar = UiFactory.boton("Actualizar", Style.AZUL);
        actualizar.addActionListener(this);
        JPanel pie = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        pie.add(actualizar);
        add(pie, BorderLayout.SOUTH);
    }

    @Override
    public void cargarDatos() {
        try {
            int[] cantidades = new ResumenDAO().obtenerCantidades();
            for (int i = 0; i < cantidades.length; i++) {
                valores[i].setText(String.valueOf(cantidades[i]));
            }
        } catch (Exception ex) {
            for (JLabel valor : valores) {
                valor.setText("No disponible");
            }
            Dialogs.error(this, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        cargarDatos();
    }
}
