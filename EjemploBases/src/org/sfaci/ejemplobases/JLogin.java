package org.sfaci.ejemplobases;

import javax.swing.*;
import java.awt.event.*;

public class JLogin extends JDialog implements ActionListener {
    private JPanel panel;
    private JButton btAceptar;
    private JButton btCancelar;
    private JTextField tfUsuario;
    private JPasswordField tfContrasena;

    private String usuario;
    private String contrasena;

    public JLogin() {
        super();
        setTitle("Login");
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
        setModal(true);

        btAceptar.addActionListener(this);
        btCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btAceptar) {
            aceptar();
        }
        else if (e.getSource() == btCancelar) {
            cancelar();
        }
    }

    private void aceptar() {
        if ((tfUsuario.getText().equals(""))
            || (tfContrasena.getPassword().toString().equals(""))) {
            JOptionPane.showMessageDialog(null,
                    "Debes introducir usuario y contraseña",
                    "Login", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuario = tfUsuario.getText();
        contrasena = tfUsuario.getText();
        setVisible(false);
    }

    private void cancelar() {
        setVisible(false);
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}
