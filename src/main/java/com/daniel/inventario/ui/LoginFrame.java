package com.daniel.inventario.ui;

import com.daniel.inventario.dao.UsuarioDAO;
import com.daniel.inventario.model.Usuario;
import com.daniel.inventario.util.Dialogs;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
        JPanel campos = new JPanel(new GridLayout(4, 1, 0, 8));
        campos.add(UiFactory.etiqueta("Usuario", txtUsuario));
        campos.add(txtUsuario);
        campos.add(UiFactory.etiqueta("Contraseña", txtPassword));
        campos.add(txtPassword);
        contenido.add(campos, BorderLayout.CENTER);
        JPanel pie = new JPanel(new BorderLayout(0, 12));
        estado.setForeground(Style.SUAVE);
        pie.add(estado, BorderLayout.NORTH);
        pie.add(btnEntrar, BorderLayout.SOUTH);
        contenido.add(pie, BorderLayout.SOUTH);
        setContentPane(contenido);
        btnEntrar.addActionListener(this);
        getRootPane().setDefaultButton(btnEntrar);
        setSize(480, 370);
        setResizable(false);
        setLocationRelativeTo(null);
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
