package com.daniel.inventario.ui;

import com.daniel.inventario.dao.ResumenDAO;
import com.daniel.inventario.util.Dialogs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DashboardPanel extends JPanel implements Refreshable, ActionListener {
    private final JLabel[] valores = new JLabel[4];
    private final JLabel estado = new JLabel("Resumen de registros del inventario.");

    public DashboardPanel(String nombre) {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(UiFactory.titulo("Bienvenido, " + nombre), BorderLayout.NORTH);
        JLabel descripcion = new JLabel("Consulta las cantidades y administra tus datos desde el menú.");
        descripcion.setForeground(Style.SUAVE);
        encabezado.add(descripcion, BorderLayout.SOUTH);
        add(encabezado, BorderLayout.NORTH);

        // Las tarjetas conservan una altura cómoda y no se estiran hasta el pie.
        JPanel cuerpo = new JPanel(new BorderLayout(0, 16));
        cuerpo.setOpaque(false);
        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 16, 16));
        tarjetas.setOpaque(false);
        String[] titulos = {"Usuarios", "Categorías", "Proveedores", "Productos"};
        String[] detalles = {"Cuentas de acceso", "Clasificación de productos",
            "Contactos de abastecimiento", "Artículos registrados"};
        for (int i = 0; i < titulos.length; i++) {
            JPanel tarjeta = new JPanel(new BorderLayout(0, 8));
            tarjeta.setPreferredSize(new Dimension(250, 152));
            tarjeta.setBackground(Style.PANEL);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Style.BORDE),
                    BorderFactory.createEmptyBorder(18, 20, 18, 20)));
            JLabel titulo = new JLabel(titulos[i]);
            titulo.setFont(Style.NEGRITA);
            titulo.setForeground(Style.TEXTO);
            valores[i] = new JLabel("—");
            valores[i].setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 36));
            valores[i].setForeground(Style.AZUL);
            JLabel detalle = new JLabel(detalles[i]);
            detalle.setForeground(Style.SUAVE);
            tarjeta.add(titulo, BorderLayout.NORTH);
            tarjeta.add(valores[i], BorderLayout.CENTER);
            tarjeta.add(detalle, BorderLayout.SOUTH);
            tarjetas.add(tarjeta);
        }
        JPanel resumen = new JPanel(new BorderLayout(0, 16));
        resumen.setOpaque(false);
        resumen.add(tarjetas, BorderLayout.CENTER);
        JPanel acciones = UiFactory.barraAcciones();
        JButton actualizar = UiFactory.boton("Actualizar", Style.AZUL);
        actualizar.addActionListener(this);
        acciones.add(actualizar);
        acciones.add(javax.swing.Box.createHorizontalStrut(16));
        estado.setForeground(Style.SUAVE);
        acciones.add(estado);
        resumen.add(acciones, BorderLayout.SOUTH);
        cuerpo.add(resumen, BorderLayout.NORTH);
        add(cuerpo, BorderLayout.CENTER);
    }

    @Override
    public void cargarDatos() {
        try {
            int[] cantidades = new ResumenDAO().obtenerCantidades();
            for (int i = 0; i < cantidades.length; i++) {
                valores[i].setText(String.valueOf(cantidades[i]));
            }
            estado.setText("Datos actualizados.");
        } catch (Exception ex) {
            for (JLabel valor : valores) {
                valor.setText("—");
            }
            estado.setText("No fue posible actualizar los datos.");
            Dialogs.error(this, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        cargarDatos();
    }
}
