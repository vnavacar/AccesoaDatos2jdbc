package org.sfsoft.holasql.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Aplicación que comprueba la conexión con MySQL
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class HolaSql {
    private JLabel lbEstado;
    private JPanel panelConectar;

    private Connection conexion;

    public HolaSql() {

    }

    /**
     * Crea una barra de menú
     * @return La barra de menú creada
     */
    public JMenuBar getMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Servidor");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Conectar");
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    conectar();
                }
            });
        menu.add(menuItem);
        menuItem = new JMenuItem("Desconectar");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                desconectar();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Salir");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                salir();
            }
        });
        menu.add(menuItem);

        return menuBar;
    }

    private void conectar() {

        /*
		 * Muestra la ventana de conexión y recoge los datos del usuario. La ventana es modal
		 * por lo que espera hasta que el usuario la cierre
		 */
        JConecta dialogoConexion = new JConecta();
        if (dialogoConexion.mostrarDialogo() == JConecta.Accion.CANCELAR)
            return;

        String servidor = dialogoConexion.getHost();
        String usuario = dialogoConexion.getUsuario();
        String contrasena = dialogoConexion.getContrasena();

        try {
			/*
			 * Carga el driver de conexión JDBC para conectar con MySQL
			 */
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + servidor + ":3306" + "/", usuario, contrasena);
            JOptionPane.showMessageDialog(null, "Se ha conectado con éxito");

        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "No se ha podido cargar el driver de la Base de Datos");
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido conectar con la Base de Datos");
            sqle.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    private void desconectar() {

        try {
            conexion.close();
            conexion = null;

            JOptionPane.showMessageDialog(null, "Se ha desconectado de la Base de Datos");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void salir() {

        desconectar();
        System.exit(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HolaSql");
        HolaSql panel = new HolaSql();
        frame.setJMenuBar(panel.getMenuBar());
        frame.setContentPane(panel.panelConectar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
