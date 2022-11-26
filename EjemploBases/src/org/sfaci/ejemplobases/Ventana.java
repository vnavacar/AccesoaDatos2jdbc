package org.sfaci.ejemplobases;

import com.toedter.calendar.JDateChooser;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import org.sfaci.ejemplobases.util.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.util.GregorianCalendar;

/**
 * Created by DAM on 16/11/2015.
 */
public class Ventana implements ActionListener {
    private JPanel panel;
    private JTabbedPane tabbedPane1;
    private JLabel lbEstado;
    private JTextField tfNombre;
    private JTextField tfApellidos;
    private JTextField tfNacionalidad;
    private JButton btNuevo;
    private JButton btGuardar;
    private JButton btModificar;
    private JButton btEliminar;
    private JButton btCancelar;
    private JTextField tfBusqueda;
    private JDateChooser dcFechaNacimiento;
    private JTable tablaCantantes;

    private Connection conexion;
    private DefaultTableModel mtCantantes;

    public Ventana() {

        initVentana();
        initTablaCantantes();

        try {
            conectar();
            login();
            listarCantantes();
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null,
                    "No se ha podido cargar el driver del SGBD",
                    "Conectar", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null,
                    "No se ha podido conectar con el servidor. Comprueba " +
                            "que está arrancado",
                    "Conectar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initVentana() {
        JFrame frame = new JFrame("Spotify");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        addListeners();
    }

    private void initTablaCantantes() {

        mtCantantes = new DefaultTableModel();
        mtCantantes.addColumn("Nombre");
        mtCantantes.addColumn("Apellidos");
        mtCantantes.addColumn("F.Nacimiento");
        mtCantantes.addColumn("Nacionalidad");

        tablaCantantes.setModel(mtCantantes);
    }

    private void listarCantantes() {

        String sql = "SELECT * FROM cantantes";
        try {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String apellidos = resultado.getString("apellidos");
                Date fechaNacimiento = resultado.getDate("fecha_nacimiento");
                String nacionalidad = resultado.getString("nacionalidad");

                Object[] fila = new Object[]{nombre, apellidos,
                    fechaNacimiento, nacionalidad};

                mtCantantes.addRow(fila);
            }
        } catch (SQLException sqle) {
            Util.mensajeError("Error al listar cantantes", "Cantantes");
            sqle.printStackTrace();
        }
    }

    private void conectar() throws ClassNotFoundException,
        SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        // FIXME coger la información de conexion
        // de un fichero de configuracion (.properties)
        conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/musica", "root", "mysql");
    }

    private void login() {

        JLogin login = new JLogin();
        login.setVisible(true);

        String usuario = login.getUsuario();
        String contrasena = login.getContrasena();

        String sql = "SELECT nombre FROM usuarios WHERE " +
                "nombre = ? AND contrasena = SHA1(?)";

        try {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, usuario);
            sentencia.setString(2, contrasena);
            ResultSet resultado = sentencia.executeQuery();

            if (!resultado.next()) {
                JOptionPane.showMessageDialog(null,
                        "Usuario/Contraseña incorrectos", "Login",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "", "",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void addListeners() {

        btNuevo.addActionListener(this);
        btGuardar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btNuevo) {
            // TODO Limpiar cajas de texto y hacerlas editables
        }
        else if (e.getSource() == btGuardar) {

            String sql = "INSERT INTO cantantes (nombre, apellidos, " +
                    "fecha_nacimiento, nacionalidad) " +
                    "VALUES (?, ?, ?, ?)";

            try {
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1, tfNombre.getText());
                sentencia.setString(2, tfApellidos.getText());
                sentencia.setDate(3, new Date(dcFechaNacimiento.getDate().getTime()));
                sentencia.setString(4, tfNacionalidad.getText());

                sentencia.executeUpdate();
            } catch (SQLException sqle) {
                Util.mensajeError("Error al dar de alta", "Alta cantantes");
            }

        }
    }
}
