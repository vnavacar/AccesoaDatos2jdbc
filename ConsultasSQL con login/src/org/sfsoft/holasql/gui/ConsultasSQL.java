package org.sfsoft.holasql.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.sfsoft.consultassql.util.Constantes;
import org.sfsoft.holasql.gui.JConecta.Accion;

/**
 * Ejemplo que consulta una Base de Datos MySQL utilizando el driver JDBC
 * utilizando un sistema de Login
 *
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class ConsultasSQL {

	private JFrame frmConsultasSql;
	private JMenuBar menuBar;
	private JMenu mnServidor;
	private JMenuItem mntmSalir;
	
	private Connection conexion;
	private JPanel panel;
	private JTextField txtFiltro;
	private JButton btBuscar;
	private JScrollPane scrollPane;
	private JTablaDatos tablaDatos;
	private JButton btCancelar;
	public JMenu mnHerramientas;
	public JMenuItem mntmPreferencias;
	
	private String nivel;
	private String usuario;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsultasSQL window = new ConsultasSQL();
					window.frmConsultasSql.setVisible(true);
					window.inicializar();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConsultasSQL() {
		initialize();
		
	}
	
	private void inicializar() {
		conectar();
		
		while (!hacerLogin());
		
		tablaDatos.setConexion(conexion);
		cargarDatos();
	}
	
	private void abrirConfiguracion() {
		
		JConfiguracion jConfig = new JConfiguracion();
		jConfig.mostrarDialogo();
	}
	
	private boolean hacerLogin() {
		
		JConecta jConecta = new JConecta();
		if (jConecta.mostrarDialogo() == JConecta.Accion.CANCELAR)
			System.exit(0);
		
		String usuario = jConecta.getUsuario();
		String contrasena = jConecta.getContrasena();
		
		try {
			String consulta = "SELECT nivel " +
					"FROM usuarios " +
					"WHERE login = '" + usuario + 
					"' AND password = SHA1('" + contrasena + "')";
			Statement sentencia = conexion.createStatement();
			ResultSet resultado = sentencia.executeQuery(consulta);
			
			if (!resultado.first())
				return false;
			
			nivel = resultado.getString(1);
			this.usuario = usuario;
			
			return true;
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, 
					"No se ha podido conectar con la Base de Datos");
			sqle.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * Carga el driver JDBC para MySQL y conecta con el SGBD
	 */
	private void conectar() {
		
		Properties configuracion = new Properties();
		
		try {
			configuracion.load(new FileInputStream("configuracion.props"));
			
			String driver = configuracion.getProperty("driver");
			String protocolo = configuracion.getProperty("protocolo");
			String servidor = configuracion.getProperty("servidor");
			String puerto = configuracion.getProperty("puerto");
			String baseDatos = configuracion.getProperty("basedatos");
			String usuario = configuracion.getProperty("usuario");
			String contrasena = configuracion.getProperty("contrasena");
		
			Class.forName(driver).newInstance();
			conexion = DriverManager.getConnection(
					protocolo + 
					servidor + ":" + puerto +
					"/" + baseDatos, 
					usuario, contrasena);
		
		} catch (FileNotFoundException fnfe) {
			JOptionPane.showMessageDialog(null, "Error al leer el fichero de configuraci�n");
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error al leer el fichero de configuraci�n");
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
		
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Sale de la aplicaci�n
	 */
	private void salir() {
		
		desconectar();
		System.exit(0);
	}
	
	/*
	 * Carga los datos en la tabla
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
	 * Aplica un filtro para realizar b�squedas
	 */
	private void buscar() {
		
		if (txtFiltro.getText().length() == 0) {
			cargarDatos();
			return;
		}
		
		if (txtFiltro.getText().length() < 2)
			return;
		
		try {
			this.tablaDatos.listar(txtFiltro.getText());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Limpiar el filtro de b�squeda y los resultados
	 */
	private void cancelarBusqueda() {
		
		this.txtFiltro.setText("");
		this.cargarDatos();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConsultasSql = new JFrame();
		frmConsultasSql.setTitle("Consultas SQL");
		frmConsultasSql.setBounds(100, 100, 450, 300);
		frmConsultasSql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConsultasSql.setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		frmConsultasSql.setJMenuBar(menuBar);
		
		mnServidor = new JMenu("Aplicaci\u00F3n");
		menuBar.add(mnServidor);
		
		mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		mnServidor.add(mntmSalir);
		
		mnHerramientas = new JMenu("Herramientas");
		menuBar.add(mnHerramientas);
		
		mntmPreferencias = new JMenuItem("Preferencias");
		mntmPreferencias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirConfiguracion();
			}
		});
		mnHerramientas.add(mntmPreferencias);
		
		panel = new JPanel();
		frmConsultasSql.getContentPane().add(panel, BorderLayout.NORTH);
		
		txtFiltro = new JTextField();
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				buscar();
			}
		});
		panel.add(txtFiltro);
		txtFiltro.setColumns(10);
		
		btBuscar = new JButton("Buscar");
		btBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscar();
			}
		});
		panel.add(btBuscar);
		
		btCancelar = new JButton("X");
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelarBusqueda();
			}
		});
		panel.add(btCancelar);
		
		scrollPane = new JScrollPane();
		frmConsultasSql.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		tablaDatos = new JTablaDatos();
		scrollPane.setViewportView(tablaDatos);
	}

}
