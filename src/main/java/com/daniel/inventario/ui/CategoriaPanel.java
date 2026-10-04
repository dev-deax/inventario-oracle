package com.daniel.inventario.ui;

import com.daniel.inventario.dao.CategoriaDAO;
import com.daniel.inventario.model.Categoria;
import java.util.ArrayList;
import javax.swing.JTextField;

public class CategoriaPanel extends BaseCrudPanel {
    private final CategoriaDAO dao = new CategoriaDAO();
    private final JTextField txtNombre = UiFactory.campo();
    private final JTextField txtDescripcion = UiFactory.campo();
    private ArrayList<Categoria> datos = new ArrayList<>();

    public CategoriaPanel() {
        super("Categorías", new String[]{"ID", "Nombre", "Descripción"});
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Descripción", txtDescripcion);
    }

    @Override
    protected void listar() throws Exception {
        datos = dao.listar();
        for (Categoria dato : datos) {
            modelo.addRow(new Object[]{dato.getId(), dato.getNombre(), dato.getDescripcion()});
        }
    }

    @Override
    protected void seleccionar(int fila) {
        Categoria dato = datos.get(fila);
        idSeleccionado = dato.getId();
        txtNombre.setText(dato.getNombre());
        txtDescripcion.setText(dato.getDescripcion());
    }

    @Override
    protected void guardar() throws Exception {
        Categoria dato = new Categoria(idSeleccionado, txtNombre.getText(), txtDescripcion.getText());
        if (idSeleccionado == null) {
            dao.insertar(dato);
        } else {
            dao.actualizar(dato);
        }
    }

    @Override
    protected void eliminar() throws Exception {
        dao.eliminar(idSeleccionado);
    }

    @Override
    protected void limpiar() {
        idSeleccionado = null;
        tabla.clearSelection();
        txtNombre.setText("");
        txtDescripcion.setText("");
    }
}
