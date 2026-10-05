package com.daniel.inventario.ui;

import com.daniel.inventario.dao.ProveedorDAO;
import com.daniel.inventario.model.Proveedor;
import java.util.ArrayList;
import javax.swing.JTextField;

public class ProveedorPanel extends BaseCrudPanel {
    private final ProveedorDAO dao = new ProveedorDAO();
    private final JTextField txtNombre = UiFactory.campo();
    private final JTextField txtEmpresa = UiFactory.campo();
    private final JTextField txtNit = UiFactory.campo();
    private final JTextField txtTelefono = UiFactory.campo();
    private final JTextField txtEmail = UiFactory.campo();
    private ArrayList<Proveedor> datos = new ArrayList<>();

    public ProveedorPanel() {
        super("Proveedores", new String[]{"ID", "Nombre", "Empresa", "NIT", "Teléfono", "Correo"});
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Empresa", txtEmpresa);
        agregarCampo("NIT", txtNit);
        agregarCampo("Teléfono", txtTelefono);
        agregarCampo("Correo", txtEmail);
        tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        int[] anchos = {50, 170, 160, 120, 130, 240};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    @Override
    protected void listar() throws Exception {
        datos = dao.listar();
        for (Proveedor dato : datos) {
            modelo.addRow(new Object[]{dato.getId(), dato.getNombre(), dato.getEmpresa(), dato.getNit(), dato.getTelefono(), dato.getEmail()});
        }
    }

    @Override
    protected void seleccionar(int fila) {
        Proveedor dato = datos.get(fila);
        idSeleccionado = dato.getId();
        txtNombre.setText(dato.getNombre());
        txtEmpresa.setText(dato.getEmpresa());
        txtNit.setText(dato.getNit());
        txtTelefono.setText(dato.getTelefono());
        txtEmail.setText(dato.getEmail());
    }

    @Override
    protected void guardar() throws Exception {
        Proveedor dato = new Proveedor(idSeleccionado, txtNombre.getText(), txtEmpresa.getText(), txtNit.getText(), txtTelefono.getText(), txtEmail.getText());
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
        txtEmpresa.setText("");
        txtNit.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }
}
