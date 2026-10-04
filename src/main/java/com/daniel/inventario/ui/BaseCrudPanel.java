package com.daniel.inventario.ui;

import com.daniel.inventario.util.Dialogs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public abstract class BaseCrudPanel extends JPanel implements Refreshable, ActionListener, ListSelectionListener {
    private final JPanel formulario = new JPanel(new GridBagLayout());
    protected final DefaultTableModel modelo;
    protected final JTable tabla;
    protected Long idSeleccionado;
    private int filaFormulario;

    protected BaseCrudPanel(String titulo, String[] columnas) {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(UiFactory.titulo(titulo), BorderLayout.NORTH);
        formulario.setBackground(Style.PANEL);
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // El formulario ocupa la izquierda y la tabla el espacio restante.
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(formulario, BorderLayout.NORTH);
        JScrollPane scrollFormulario = new JScrollPane(contenedor);
        scrollFormulario.setPreferredSize(new Dimension(290, 0));
        scrollFormulario.setBorder(BorderFactory.createLineBorder(Style.BORDE));
        add(scrollFormulario, BorderLayout.WEST);

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(this);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionBackground(new java.awt.Color(204, 224, 246));
        tabla.setSelectionForeground(Style.TEXTO);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setColumnHeaderView(tabla.getTableHeader());
        scrollTabla.getViewport().setBackground(Style.PANEL);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        agregarBoton(acciones, "Nuevo", Style.SUAVE);
        agregarBoton(acciones, "Guardar", Style.VERDE);
        agregarBoton(acciones, "Eliminar", Style.ROJO);
        agregarBoton(acciones, "Actualizar", Style.AZUL);
        add(acciones, BorderLayout.SOUTH);
    }

    protected void agregarCampo(String nombre, JComponent campo) {
        GridBagConstraints posicion = new GridBagConstraints();
        posicion.gridx = 0;
        posicion.gridy = filaFormulario;
        posicion.weightx = 1;
        posicion.fill = GridBagConstraints.HORIZONTAL;
        posicion.insets = new Insets(0, 0, 4, 0);
        formulario.add(UiFactory.etiqueta(nombre, campo), posicion);
        posicion.gridy = filaFormulario + 1;
        posicion.insets = new Insets(0, 0, 12, 0);
        formulario.add(campo, posicion);
        filaFormulario += 2;
    }

    private void agregarBoton(JPanel panel, String texto, java.awt.Color color) {
        JButton boton = UiFactory.boton(texto, color);
        boton.addActionListener(this);
        panel.add(boton);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        try {
            if (accion.equals("Nuevo")) {
                limpiar();
            } else if (accion.equals("Actualizar")) {
                cargarDatos();
            } else if (accion.equals("Guardar")) {
                guardar();
                Dialogs.exito(this, "Registro guardado correctamente.");
                cargarDatos();
            } else if (accion.equals("Eliminar")) {
                if (idSeleccionado == null) {
                    throw new IllegalArgumentException("Selecciona un registro en la tabla.");
                }
                if (Dialogs.confirmar(this, "¿Deseas eliminar el registro seleccionado?")) {
                    eliminar();
                    Dialogs.exito(this, "Registro eliminado.");
                    cargarDatos();
                }
            }
        } catch (Exception ex) {
            Dialogs.error(this, ex);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent evento) {
        if (!evento.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
            seleccionar(tabla.getSelectedRow());
        }
    }

    @Override
    public final void cargarDatos() {
        limpiar();
        modelo.setRowCount(0);
        try {
            listar();
        } catch (Exception ex) {
            Dialogs.error(this, ex);
        }
    }

    protected abstract void listar() throws Exception;
    protected abstract void seleccionar(int fila);
    protected abstract void guardar() throws Exception;
    protected abstract void eliminar() throws Exception;
    protected abstract void limpiar();
}
