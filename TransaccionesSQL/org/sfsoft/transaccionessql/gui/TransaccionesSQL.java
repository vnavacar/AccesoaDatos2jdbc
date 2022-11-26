package org.sfsoft.transaccionessql.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.sfsoft.transaccionessql.util.Constantes.AccionDialogo;

/**
 * Ejemplo que consulta y lanza sentencias de inserción, modificación y eliminación contra una Base de Datos 
 * MySQL utilizando el driver JDBC.
 *  - Se emplea la clase PreparedStatement en lugar de Statement
 *  - Permite seleccionar de un ComboBox la Base de Datos con la que el usuario desea conectar
 * 	- Invoca a una función almacenada de MySQL para mostrar la puntuación total de los personajes
 *  - Invoca a un procedimiento almaceado para eliminar todos los personajes
 * @author Santiago Faci
 * @version 1.0
 */
public class TransaccionesSQL {

	private JFrame frmTransaccionesSql;
	private JMenuBar menuBar;
	private JMenu mnServidor;
	private JMenuItem mntmConectar;
	private JMenuItem mntmDesconectar;
	private JMenuItem mntmSalir;
	private JScrollPane scrollPane;
	private JTablaDatos tablaDatos;
	private JPanel panelDatos;
	private JPanel panelSuperior;
	private JButton btNuevo;
	
	private Connection conexion;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransaccionesSQL window = new TransaccionesSQL();
					window.frmTransaccionesSql.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TransaccionesSQL() {
		initialize();
		inicializar();
	}
	
	/*
	 * Inicializa algunos aspectos del interfaz antes de que el usuario tome el cotnrol del mismo
	 */
	private void inicializar() {
		
		// Coloca el JFrame en el centro de la pantalla. También funciona con JDialog
		frmTransaccionesSql.setLocationRelativeTo(null);
	}
	
	/*
	 * Carga el driver JDBC para MySQL y conecta con el SGBD
	 */
	private void conectar() {
		
		JConecta jConecta = new JConecta();
		if (jConecta.mostrarDialogo() == AccionDialogo.CANCELAR)
			return;
		
		String servidor = jConecta.getServidor();
		String baseDatos = jConecta.getBaseDatos();
		String usuario = jConecta.getUsuario();
		String contrasena = jConecta.getContrasena();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conexion = DriverManager.getConnection("jdbc:mysql://" + servidor + ":3306" + "/" + baseDatos, 
					usuario, contrasena);
			JOptionPane.showMessageDialog(null, "Se ha conectado con éxito");
			
			tablaDatos.setConexion(conexion);
			cargarDatos();
			
			activarControles();
			
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
	
	/*
	 * Desconecta de MySQL
	 */
	private void desconectar() {
		
		try {
			conexion.close();
			conexion = null;	
			
			JOptionPane.showMessageDialog(null, "Se ha desconectado de la Base de Datos");
			
			desactivarControles();
			tablaDatos.vaciar();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Sale de la aplicación
	 */
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
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Da de alta un nuevo personaje
	 */
	private void nuevo() {
		
		JEnemigo jEnemigo = new JEnemigo();
		jEnemigo.setConexion(conexion);
		if (jEnemigo.mostrarDialogo() == AccionDialogo.CANCELAR)
			return;
		
		try {
			if (tablaDatos.existe(jEnemigo.getNombre())) {
				JOptionPane.showMessageDialog(null, "Ya existe un enemigo con este nombre", "Alta", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			tablaDatos.nuevo(jEnemigo.getNombre(), jEnemigo.getEnergia(), jEnemigo.getDano(), 
					jEnemigo.getPantallas());
			cargarDatos();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "No se ha podido registrar el enemigo.\n"
					+ "Se ha producido un error", "Alta", JOptionPane.ERROR_MESSAGE);
			
			// En caso de fallo se deshacen todas las operaciones no validadas
			try {
				conexion.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sqle.printStackTrace();
		} finally {
			try {
				conexion.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Activa los controles del interfaz de usuario
	 */
	private void activarControles() {
		
		this.btNuevo.setEnabled(true);
		
		mntmConectar.setEnabled(false);
		mntmDesconectar.setEnabled(true);
	}
	
	/*
	 * Desactiva los controles del interfaz de usuario
	 */
	private void desactivarControles() {
		
		this.btNuevo.setEnabled(false);
		
		mntmConectar.setEnabled(true);
		mntmDesconectar.setEnabled(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTransaccionesSql = new JFrame();
		frmTransaccionesSql.setTitle("Transacciones SQL");
		frmTransaccionesSql.setBounds(100, 100, 450, 300);
		frmTransaccionesSql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frmTransaccionesSql.setJMenuBar(menuBar);
		
		mnServidor = new JMenu("Servidor");
		menuBar.add(mnServidor);
		
		mntmConectar = new JMenuItem("Conectar");
		mntmConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectar();
			}
		});
		mnServidor.add(mntmConectar);
		
		mntmDesconectar = new JMenuItem("Desconectar");
		mntmDesconectar.setEnabled(false);
		mntmDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectar();
			}
		});
		mnServidor.add(mntmDesconectar);
		
		mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		mnServidor.add(mntmSalir);
		
		panelSuperior = new JPanel();
		frmTransaccionesSql.getContentPane().add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(0, 0));
		
		panelDatos = new JPanel();
		panelSuperior.add(panelDatos, BorderLayout.SOUTH);
		
		btNuevo = new JButton("Nuevo");
		btNuevo.setEnabled(false);
		btNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nuevo();
			}
		});
		panelDatos.add(btNuevo);
		
		scrollPane = new JScrollPane();
		frmTransaccionesSql.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		tablaDatos = new JTablaDatos();
		scrollPane.setViewportView(tablaDatos);
	}

}
