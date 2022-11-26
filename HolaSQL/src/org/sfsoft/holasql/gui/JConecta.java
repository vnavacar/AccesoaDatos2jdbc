package org.sfsoft.holasql.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Diálogo para recoger los datos de conexión con un SGBD
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class JConecta extends JDialog {
    private JPanel panel1;
    private JTextField tfUsuario;
    private JPasswordField tfContrasena;
    private JButton btConectar;
    private JTextField tfHost;
    private JButton btCancelar;

    private String host;
    private String usuario;
    private String contrasena;
    public enum Accion {
        ACEPTAR, CANCELAR
    }
    private Accion accion;

    public JConecta() {

        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        btConectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptar();
            }
        });

        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    /**
     * El usuario ha pulsado aceptar. Se recogen los datos del formulario como atributos de la clase
     * y se esconde el formulario
     */
    private void aceptar() {

        host = tfHost.getText();
        usuario = tfUsuario.getText();
        contrasena = String.valueOf(tfContrasena.getPassword());

        accion = Accion.ACEPTAR;
        setVisible(false);
    }

    /**
     * El usuario cancela. Se esconde el formulario
     */
    private void cancelar() {

        accion = Accion.CANCELAR;
        setVisible(false);
    }

	/*
	 * Getters para la recogida de información del formulario desde la ventana principal
	 * de la aplicación
	 */

    public String getHost() {
        return host;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Accion mostrarDialogo() {

        setVisible(true);

        return accion;
    }
}
