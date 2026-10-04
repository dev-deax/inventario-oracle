package com.daniel.inventario.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

public class Style {
    public static final Color FONDO = new Color(247, 250, 252);
    public static final Color PANEL = Color.WHITE;
    public static final Color TEXTO = new Color(26, 32, 44);
    public static final Color SUAVE = new Color(74, 85, 104);
    public static final Color AZUL = new Color(36, 86, 146);
    public static final Color VERDE = new Color(35, 112, 69);
    public static final Color ROJO = new Color(176, 42, 42);
    public static final Color BORDE = new Color(155, 166, 180);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font NEGRITA = new Font("SansSerif", Font.BOLD, 14);
    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 26);

    public static void aplicar() {
        String[] controles = {"Label", "Button", "TextField", "PasswordField", "ComboBox",
            "Table", "TableHeader", "CheckBox", "OptionPane"};
        for (String control : controles) {
            UIManager.put(control + ".font", NORMAL);
        }
        UIManager.put("OptionPane.messageFont", NORMAL);
        UIManager.put("Panel.background", FONDO);
    }
}
