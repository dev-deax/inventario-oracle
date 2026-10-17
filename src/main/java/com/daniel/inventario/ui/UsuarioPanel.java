package com.daniel.inventario.ui;

import com.daniel.inventario.dao.UsuarioDAO;
import com.daniel.inventario.model.Usuario;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UsuarioPanel extends BaseCrudPanel {
    private final UsuarioDAO dao = new UsuarioDAO();
    private final Usuario sesion;
    private final JTextField txtUsuario = UiFactory.campo();
    private final JTextField txtNombre = UiFactory.campo();
    private final JTextField txtApellido = UiFactory.campo();
    private final JPasswordField txtPassword = UiFactory.password();
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public UsuarioPanel(Usuario sesion) {
        super("Usuarios", new String[]{"ID", "Usuario", "Nombre", "Apellido", "Fecha de creación"});
        this.sesion = sesion;
        agregarCampo("Usuario", txtUsuario);
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Apellido", txtApellido);
        agregarCampo("Contraseña (mínimo 8 caracteres)", txtPassword);
        txtPassword.setToolTipText("Al editar, deja vacío para conservar la contraseña.");
    }

    @Override
    protected void listar() throws Exception {
        usuarios = dao.buscar(txtBusqueda.getText());
        for (Usuario usuario : usuarios) {
            modelo.addRow(new Object[]{usuario.getId(), usuario.getUsuario(), usuario.getNombre(), usuario.getApellido(), UiFactory.fechaCreacion(usuario.getCreadoEn())});
        }
    }

    @Override
    protected void seleccionar(int fila) {
        Usuario usuario = usuarios.get(fila);
        idSeleccionado = usuario.getId();
        txtUsuario.setText(usuario.getUsuario());
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtPassword.setText("");
    }

    @Override
    protected void guardar() throws Exception {
        Usuario usuario = new Usuario(idSeleccionado, txtUsuario.getText(), txtNombre.getText(), txtApellido.getText());
        char[] password = txtPassword.getPassword();
        try {
            if (idSeleccionado == null) {
                dao.insertar(usuario, password);
            } else {
                dao.actualizar(usuario, password);
            }
        } finally {
            Arrays.fill(password, '\0');
            txtPassword.setText("");
        }
    }

    @Override
    protected void eliminar() throws Exception {
        dao.eliminar(idSeleccionado, sesion.getId());
    }

    @Override
    protected void limpiar() {
        idSeleccionado = null;
        tabla.clearSelection();
        txtUsuario.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtPassword.setText("");
    }
}
