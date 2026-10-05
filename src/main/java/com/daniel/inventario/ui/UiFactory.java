package com.daniel.inventario.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComponent;

public class UiFactory {
    public static JTextField campo() {
        JTextField campo = new JTextField(18);
        prepararCampo(campo);
        return campo;
    }

    public static JPasswordField password() {
        JPasswordField campo = new JPasswordField(18);
        prepararCampo(campo);
        return campo;
    }

    private static void prepararCampo(JTextField campo) {

        campo.setMargin(new java.awt.Insets(6, 8, 6, 8));
        Dimension natural = campo.getPreferredSize();
        campo.setPreferredSize(new Dimension(Math.max(230, natural.width), Math.max(36, natural.height)));
    }

    public static JButton boton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(Style.NEGRITA);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(130, 40));
        return boton;
    }

    public static JLabel etiqueta(String texto, JComponent campo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(Style.TEXTO);
        etiqueta.setLabelFor(campo);
        campo.getAccessibleContext().setAccessibleName(texto);
        return etiqueta;
    }

    public static JLabel titulo(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(Style.TITULO);
        titulo.setForeground(Style.TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        return titulo;
    }
}
