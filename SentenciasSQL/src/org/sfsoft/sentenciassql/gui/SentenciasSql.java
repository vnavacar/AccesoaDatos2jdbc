package org.sfsoft.sentenciassql.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

/**
 * Aplicación que ejecuta sentencias SQL, procedimientos y funciones en MySQL
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class SentenciasSql {
    private JLabel lbEstado;
    private JPanel panelConectar;
    private JButton btNuevo;
    private JButton btModificar;
    private JButton btEliminar;
    private JTextField tfFiltro;
    private JLabel lbPuntuacion;
    private JButton btCancelarBusqueda;
    private JButton btEliminarTodos;
    private JScrollPane scrollPane;
    private JTablaDatos tablaDatos;

    private Connection conexion;

    public SentenciasSql() {

        btNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });

        btModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });

        btEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        btEliminarTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTodos();
            }
        });

        tfFiltro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                buscar();
            }
        });

        btCancelarBusqueda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarBusqueda();
            }
        });
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

        String host = dialogoConexion.getHost();
        String usuario = dialogoConexion.getUsuario();
        String contrasena = dialogoConexion.getContrasena();
        String catalogo = dialogoConexion.getCatalogo();

        try {
			/*
			 * Carga el driver de conexión JDBC para conectar con MySQL
			 */
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":3306" + "/" + catalogo, usuario, contrasena);
            lbEstado.setText("Se ha conectado con éxito");
            tablaDatos.setConexion(conexion);
            cargarDatos();

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

    /*
	 * Carga los datos en la tabla, según la Base de Datos a la que ha conectado el usuario
	 */
    private void cargarDatos() {

        try {
            tablaDatos.listar();

            if (tablaDatos.getRowCount() == 0)
                JOptionPane.showMessageDialog(null, "No hay datos que mostrar");

            mostrarPuntuacionTotal();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Muestra la puntuación total invocando a una función almacenada de MySQL
     * @throws SQLException
     */
    private void mostrarPuntuacionTotal() throws SQLException {

        PreparedStatement sentencia = conexion.prepareStatement("select get_total_puntos()");
        ResultSet resultado = sentencia.executeQuery();
        resultado.next();

        String puntos = resultado.getString(1);
        if (resultado.wasNull())
            lbPuntuacion.setText("Total Puntos: 0");
        else
            lbPuntuacion.setText("Total Puntos: " + puntos);

        // Libera los recursos del cursor (ResultSet)
        try {
            resultado.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Realiza la búsqueda según el filtro introducido por el usuario en la caja de texto
     */
    private void buscar() {

        if (tfFiltro.getText().length() == 0) {
            cargarDatos();
            return;
        }

        if (tfFiltro.getText().length() < 2)
            return;

        try {
            tablaDatos.listar(tfFiltro.getText());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * El usuario cancela el filtrado de datos. Vuelven a mostrarse todos los registros en la tabla
     */
    private void cancelarBusqueda() {

        tfFiltro.setText("");
        cargarDatos();
    }

    /**
     * Da de alta un nuevo personaje
     */
    private void nuevo() {

        JPersonaje jPersonaje = new JPersonaje();
        if (jPersonaje.mostrarDialogo() == JPersonaje.Accion.CANCELAR)
            return;

        try {
            if (tablaDatos.existe(jPersonaje.getNombre())) {
                JOptionPane.showMessageDialog(null, "Ya existe un personaje con este nombre", "Alta",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            tablaDatos.nuevo(jPersonaje.getNombre(), jPersonaje.getNivel(),
                    jPersonaje.getEnergia(), jPersonaje.getPuntos());
            cargarDatos();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido registrar el personaje.\n"
                    + "Se ha producido un error", "Alta", JOptionPane.ERROR_MESSAGE);

            sqle.printStackTrace();
        }
    }

    /**
     * Modifica la información del personaje que el usuario ha seleccionado en la tabla
     */
    private void modificar() {

        int filaSeleccionada = 0;

        filaSeleccionada = tablaDatos.getSelectedRow();
        if (filaSeleccionada == -1)
            return;

        JPersonaje jPersonaje = new JPersonaje();
        jPersonaje.setNombre((String) tablaDatos.getValueAt(filaSeleccionada, 0));
        jPersonaje.setNivel(Integer.parseInt((String) tablaDatos.getValueAt(filaSeleccionada, 1)));
        jPersonaje.setEnergia(Integer.parseInt((String) tablaDatos.getValueAt(filaSeleccionada, 2)));
        jPersonaje.setPuntos(Integer.parseInt((String) tablaDatos.getValueAt(filaSeleccionada, 3)));

        if (jPersonaje.mostrarDialogo() == JPersonaje.Accion.CANCELAR)
            return;

        try {
            String nombreOriginal = (String) tablaDatos.getValueAt(filaSeleccionada, 0);

            // Si ha modificado el nombre hay que comprobar que el nuevo nombre no exista ya
            if (!nombreOriginal.equals(jPersonaje.getNombre()))
                if (tablaDatos.existe(jPersonaje.getNombre())) {
                    JOptionPane.showMessageDialog(null, "Ya existe un personaje con este nombre", "Alta",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

            tablaDatos.modificar(nombreOriginal, jPersonaje.getNombre(), jPersonaje.getNivel(), jPersonaje.getEnergia(),
                    jPersonaje.getPuntos());
            cargarDatos();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido modificar el personaje\n"
                    + "Se ha producido un error", "Modificar", JOptionPane.ERROR_MESSAGE);
            sqle.printStackTrace();
        }
    }

    /*
     * El usuario elimina el personaje que ha seleccionado de la tabla
     */
    private void eliminar() {

        if (JOptionPane.showConfirmDialog(null, "¿Está seguro?", "Eliminar", JOptionPane.YES_NO_OPTION) ==
                JOptionPane.NO_OPTION)
            return;

        try {
            tablaDatos.eliminar();

            JOptionPane.showMessageDialog(null, "El personaje se ha eliminado correctamente");

            cargarDatos();

        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido eliminar el personaje.\n" +
                    "Se ha producido un error", "Eliminar", JOptionPane.ERROR_MESSAGE);
            sqle.printStackTrace();
        }
    }

    /*
     * Elimina todos los personajes de la Base de Datos utilizando un procedimiento almacenado
     */
    private void eliminarTodos() {

        if (JOptionPane.showConfirmDialog(null, "¿Está seguro?", "Eliminar", JOptionPane.YES_NO_OPTION) ==
                JOptionPane.NO_OPTION)
            return;

        try {
            CallableStatement procedimiento = conexion.prepareCall("{ call eliminar_personajes() }");
            procedimiento.execute();

            cargarDatos();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Activa los controles del interfaz de usuario
     */
    private void activarControles() {

        tfFiltro.setEnabled(true);
        btCancelarBusqueda.setEnabled(true);
        btNuevo.setEnabled(true);
        btModificar.setEnabled(true);
        btEliminar.setEnabled(true);
        btEliminarTodos.setEnabled(true);
    }

    /**
     * Desactiva los controles del interfaz de usuario
     */
    private void desactivarControles() {

        tfFiltro.setEnabled(false);
        btCancelarBusqueda.setEnabled(false);
        btNuevo.setEnabled(false);
        btModificar.setEnabled(false);
        btEliminar.setEnabled(false);
        btEliminarTodos.setEnabled(false);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SentenciasSQL");
        SentenciasSql panel = new SentenciasSql();
        frame.setJMenuBar(panel.getMenuBar());
        frame.setContentPane(panel.panelConectar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
