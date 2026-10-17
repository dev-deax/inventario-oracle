package com.daniel.inventario.ui;

import com.daniel.inventario.dao.CategoriaDAO;
import com.daniel.inventario.dao.ProductoDAO;
import com.daniel.inventario.dao.ProveedorDAO;
import com.daniel.inventario.model.Categoria;
import com.daniel.inventario.model.Producto;
import com.daniel.inventario.model.Proveedor;
import com.daniel.inventario.util.Validators;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class ProductoPanel extends BaseCrudPanel {
    private final ProductoDAO dao = new ProductoDAO();
    private final JTextField txtCodigo = UiFactory.campo();
    private final JTextField txtNombre = UiFactory.campo();
    private final JTextField txtPrecio = UiFactory.campo();
    private final JTextField txtStock = UiFactory.campo();
    private final JComboBox<Categoria> cboCategoria = new JComboBox<>();
    private final JComboBox<Proveedor> cboProveedor = new JComboBox<>();
    private final JCheckBox chkActivo = new JCheckBox("Producto activo", true);
    private ArrayList<Producto> productos = new ArrayList<>();

    public ProductoPanel() {
        super("Productos", new String[]{"ID", "Código", "Nombre", "Precio", "Stock", "Categoría", "Proveedor", "Activo", "Fecha de creación"});
        agregarCampo("Código", txtCodigo);
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Precio", txtPrecio);
        agregarCampo("Stock", txtStock);
        agregarCampo("Categoría", cboCategoria);
        agregarCampo("Proveedor", cboProveedor);
        agregarCampo("Estado", chkActivo);
        chkActivo.setOpaque(false);
        tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(110);
        }
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
    }

    @Override
    protected void listar() throws Exception {
        cboCategoria.removeAllItems();
        cboProveedor.removeAllItems();
        ArrayList<Categoria> categorias = new CategoriaDAO().listar();
        ArrayList<Proveedor> proveedores = new ProveedorDAO().listar();
        for (Categoria categoria : categorias) {
            cboCategoria.addItem(categoria);
        }
        for (Proveedor proveedor : proveedores) {
            cboProveedor.addItem(proveedor);
        }
        productos = dao.buscar(txtBusqueda.getText());
        for (Producto producto : productos) {
            String estado = "No";
            if (producto.isActivo()) {
                estado = "Sí";
            }
            modelo.addRow(new Object[]{producto.getId(), producto.getCodigo(), producto.getNombre(),
                producto.getPrecio(), producto.getStock(), producto.getCategoria().getNombre(),
                producto.getProveedor().getNombre(), estado, UiFactory.fechaCreacion(producto.getCreadoEn())});
        }
    }

    @Override
    protected void seleccionar(int fila) {
        Producto producto = productos.get(fila);
        idSeleccionado = producto.getId();
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        txtPrecio.setText(producto.getPrecio().toPlainString());
        txtStock.setText(String.valueOf(producto.getStock()));
        chkActivo.setSelected(producto.isActivo());
        cboCategoria.setSelectedIndex(-1);
        cboProveedor.setSelectedIndex(-1);
        for (int i = 0; i < cboCategoria.getItemCount(); i++) {
            if (cboCategoria.getItemAt(i).getId().equals(producto.getCategoria().getId())) {
                cboCategoria.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < cboProveedor.getItemCount(); i++) {
            if (cboProveedor.getItemAt(i).getId().equals(producto.getProveedor().getId())) {
                cboProveedor.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    protected void guardar() throws Exception {
        Categoria categoria = (Categoria) cboCategoria.getSelectedItem();
        Proveedor proveedor = (Proveedor) cboProveedor.getSelectedItem();
        if (categoria == null || proveedor == null) {
            throw new IllegalArgumentException("Primero crea y selecciona una categoría y un proveedor.");
        }
        Producto producto = new Producto(idSeleccionado, txtCodigo.getText(), txtNombre.getText(),
                Validators.precio(txtPrecio.getText()), Validators.stock(txtStock.getText()),
                categoria, proveedor, chkActivo.isSelected());
        if (idSeleccionado == null) {
            dao.insertar(producto);
        } else {
            dao.actualizar(producto);
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
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        chkActivo.setSelected(true);
        cboCategoria.setSelectedIndex(-1);
        cboProveedor.setSelectedIndex(-1);
    }
}
