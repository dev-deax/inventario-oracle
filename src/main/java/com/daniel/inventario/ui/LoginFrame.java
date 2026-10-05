package com.daniel.inventario.ui;

import com.daniel.inventario.dao.UsuarioDAO;
import com.daniel.inventario.model.Usuario;
import com.daniel.inventario.util.Dialogs;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class LoginFrame extends JFrame implements ActionListener {
    private final JTextField txtUsuario = UiFactory.campo();
    private final JPasswordField txtPassword = UiFactory.password();
    private final JButton btnEntrar = UiFactory.boton("Ingresar", Style.AZUL);
    private final JLabel estado = new JLabel("Ingresa tus credenciales para continuar.");

    public LoginFrame() {
        super("Inventario Oracle - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contenido = new JPanel(new BorderLayout(0, 14));
        contenido.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        contenido.add(UiFactory.titulo("Inventario Oracle"), BorderLayout.NORTH);

        // Cada etiqueta queda encima del campo correspondiente.
        JPanel campos = new JPanel(new GridBagLayout());
        agregarCampo(campos, "Usuario", txtUsuario, 0);
        agregarCampo(campos, "Contraseña", txtPassword, 2);
        contenido.add(campos, BorderLayout.CENTER);
        JPanel pie = new JPanel(new BorderLayout(0, 12));
        estado.setForeground(Style.SUAVE);
        pie.add(estado, BorderLayout.NORTH);
        pie.add(btnEntrar, BorderLayout.SOUTH);
        contenido.add(pie, BorderLayout.SOUTH);
        setContentPane(contenido);
        btnEntrar.addActionListener(this);
        getRootPane().setDefaultButton(btnEntrar);
        // pack respeta la altura de los campos y la fuente del sistema.
        pack();
        setSize(Math.max(480, getWidth()), getHeight());
        setMinimumSize(new Dimension(getWidth(), getHeight()));
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo, int fila) {
        GridBagConstraints posicion = new GridBagConstraints();
        posicion.gridx = 0;
        posicion.gridy = fila;
        posicion.weightx = 1;
        posicion.fill = GridBagConstraints.HORIZONTAL;
        posicion.anchor = GridBagConstraints.WEST;
        posicion.insets = new Insets(0, 0, 6, 0);
        panel.add(UiFactory.etiqueta(etiqueta, campo), posicion);
        posicion.gridy = fila + 1;
        posicion.insets = new Insets(0, 0, 14, 0);
        panel.add(campo, posicion);
    }
    @Override
    public void actionPerformed(ActionEvent evento) {
        final String usuario = txtUsuario.getText();
        final char[] password = txtPassword.getPassword();
        if (usuario.isBlank() || password.length == 0) {
            Arrays.fill(password, '\0');
            Dialogs.error(this, new IllegalArgumentException("Ingresa tu usuario y contraseña."));
            return;
        }
        btnEntrar.setEnabled(false);
        estado.setText("Verificando acceso...");

        // La consulta y el hash se ejecutan aparte para no congelar la ventana.
        SwingWorker<Usuario, Void> tarea = new SwingWorker<Usuario, Void>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                try {
                    return new UsuarioDAO().autenticar(usuario, password);
                } finally {
                    Arrays.fill(password, '\0');
                }
            }

            @Override
            protected void done() {
                btnEntrar.setEnabled(true);
                estado.setText("Ingresa tus credenciales para continuar.");
                txtPassword.setText("");
                try {
                    Usuario cuenta = get();
                    if (cuenta == null) {
                        Dialogs.error(LoginFrame.this, new IllegalArgumentException("Usuario o contraseña incorrectos."));
                        txtPassword.requestFocusInWindow();
                    } else {
                        accesoCorrecto(cuenta);
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    Dialogs.error(LoginFrame.this, ex);
                } catch (ExecutionException ex) {
                    Throwable causa = ex.getCause();
                    if (causa instanceof Exception) {
                        Dialogs.error(LoginFrame.this, (Exception) causa);
                    } else {
                        Dialogs.error(LoginFrame.this, ex);
                    }
                }
            }
        };
        tarea.execute();
    }

    private void accesoCorrecto(Usuario usuario) {
        Dialogs.exito(this, "Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre() + ".");
        new MainFrame(usuario).setVisible(true);
        dispose();
    }
}
