package com.daniel.inventario;

import com.daniel.inventario.ui.LoginFrame;
import com.daniel.inventario.ui.Style;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Style.aplicar();
                new LoginFrame().setVisible(true);
            }
        });
    }
}
