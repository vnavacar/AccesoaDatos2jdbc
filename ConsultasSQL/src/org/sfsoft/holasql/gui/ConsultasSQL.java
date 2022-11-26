package org.sfsoft.holasql.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.sfsoft.holasql.gui.JConecta.Accion;

/**
 * Ejemplo que consulta una Base de Datos MySQL utilizando el driver JDBC
 * @author Santiago Faci
 *
 */
public class ConsultasSQL {

	private JFrame frmConsultasSql;
	private JMenuBar menuBar;
	private JMenu mnServidor;
	private JMenuItem mntmConectar;
	private JMenuItem mntmDesconectar;
	private JMenuItem mntmSalir;
	
	private Connection conexion;
	private JPanel panel;
	private JTextField txtFiltro;
	private JButton btBuscar;
	private JScrollPane scrollPane;
	private JTablaDatos tablaDatos;
	private JButton btCancelar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsultasSQL window = new ConsultasSQL();
					window.frmConsultasSql.setVisible(true);
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
		inicializar();
	}
	
	private void inicializar() {
	}
	
	/*
	 * Carga el driver JDBC para MySQL y conecta con el SGBD
	 */
	private void conectar() {
		
		JConecta jConecta = new JConecta();
		
		if (jConecta.mostrarDialogo() == Accion.CANCELAR) {
			return;
		}
		
		String servidor = jConecta.getServidor();
		String baseDatos = jConecta.getBaseDatos();
		String usuario = jConecta.getUsuario();
		String contrasena = jConecta.getContrasena();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conexion = DriverManager.getConnection("jdbc:mysql://" + servidor + ":3306" + "/" + baseDatos, 
					usuario, contrasena);
			JOptionPane.showMessageDialog(null, "Se ha conectado con éxito");
			
			mntmConectar.setEnabled(false);
			mntmDesconectar.setEnabled(true);
			
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
	
	/*
	 * Desconecta de MySQL
	 */
	private void desconectar() {
		
		try {
			conexion.close();
			conexion = null;	
			
			JOptionPane.showMessageDialog(null, "Se ha desconectado de la Base de Datos");
			
			mntmConectar.setEnabled(true);
			mntmDesconectar.setEnabled(false);
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
	 * Aplica un filtro para realizar búsquedas
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
	 * Limpiar el filtro de búsqueda y los resultados
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
