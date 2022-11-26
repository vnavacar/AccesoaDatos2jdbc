package org.sfsoft.holasql.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private JComboBox<String> cbCatalogo;

    private String host;
    private String usuario;
    private String contrasena;
    private String catalogo;
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

        cbCatalogo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                listarCatalogo();
            }
        });
    }

    /**
     * Lista las bases de datos disponibles en el servidor en un Combo
     * Se ejecuta este método cuando el combo obtiene el foco (focusGained)
     */
    private void listarCatalogo() {

        if (cbCatalogo.getItemCount() > 0)
            return;

        Connection conexion = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + tfHost.getText() + ":3306"
                    , tfUsuario.getText(), String.valueOf(tfContrasena.getPassword()));

            // Obtiene información sobre las Bases de Datos que hay en el servidor
            ResultSet catalogo = conexion.getMetaData().getCatalogs();

            while (catalogo.next()) {
                cbCatalogo.addItem(catalogo.getString(1));
            }
            catalogo.close();

        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    /**
     * El usuario ha pulsado aceptar. Se recogen los datos del formulario como atributos de la clase
     * y se esconde el formulario
     */
    private void aceptar() {

        host = tfHost.getText();
        usuario = tfUsuario.getText();
        contrasena = String.valueOf(tfContrasena.getPassword());
        catalogo = (String) cbCatalogo.getSelectedItem();

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

    public String getCatalogo() { return catalogo; }

    public Accion mostrarDialogo() {

        setVisible(true);

        return accion;
    }
}
