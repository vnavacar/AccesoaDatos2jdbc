package org.sfsoft.transaccionessql.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.sfsoft.transaccionessql.util.Constantes.AccionDialogo;;

/**
 * Diálogo para recoger los datos de conexión con un SGBD
 * Muestra en un ComboBox las Bases de Datos disponibles para el usuario
 * @author Santiago Faci
 * @version 2.0
 */
public class JConecta extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtServidor;
	private JTextField txtUsuario;
	private JPasswordField txtContrasena;
	private JLabel lblBaseDeDatos;
	private JComboBox cbCatalogo;
	
	private String servidor;
	private String baseDatos;
	private String usuario;
	private String contrasena;
	private AccionDialogo accion;
	
	/**
	 * Lista las bases de datos disponibles en el servidor en un Combo
	 */
	private void listarCatalogo() {
		
		if (cbCatalogo.getItemCount() > 0)
			return;
		
		Connection conexion = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conexion = DriverManager.getConnection("jdbc:mysql://" + txtServidor.getText() + ":3306" 
					, txtUsuario.getText(), String.valueOf(txtContrasena.getPassword()));
			
			// Obtiene información sobre las Bases de Datos que hay en el servidor
			ResultSet catalogo = conexion.getMetaData().getCatalogs();
			
			while (catalogo.next()) {
				cbCatalogo.addItem((String) catalogo.getString(1));
			}
			catalogo.close();
			catalogo = null;
			
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
		
		servidor = txtServidor.getText();
		baseDatos = (String) cbCatalogo.getSelectedItem();
		usuario = txtUsuario.getText();
		contrasena = String.valueOf(txtContrasena.getPassword());
		
		accion = AccionDialogo.ACEPTAR;
		setVisible(false);
	}
	
	/**
	 * El usuario cancela. Se esconde el formulario 
	 */
	private void cancelar() {
		
		accion = AccionDialogo.CANCELAR;
		setVisible(false);
	}
	
	/*
	 * Getters para la recogida de información del formulario desde la ventana principal 
	 * de la aplicación
	 */
	
	public String getServidor() {
		return servidor;
	}
	
	public String getBaseDatos() {
		return baseDatos;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public String getContrasena() {
		return contrasena;
	}
	
	public AccionDialogo mostrarDialogo() {
		
		setVisible(true);
		return accion;
	}
	
	/**
	 * Crea el diálogo y lo muestra en pantalla
	 */
	public JConecta() {
		setModal(true);
		setTitle("Conectar");
		setBounds(100, 100, 271, 191);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblServidor = new JLabel("Servidor");
		lblServidor.setBounds(10, 7, 66, 14);
		contentPanel.add(lblServidor);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(10, 38, 66, 14);
		contentPanel.add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a");
		lblContrasea.setBounds(10, 69, 66, 14);
		contentPanel.add(lblContrasea);
		
		txtServidor = new JTextField();
		txtServidor.setText("localhost");
		txtServidor.setBounds(123, 4, 106, 20);
		contentPanel.add(txtServidor);
		txtServidor.setColumns(10);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(123, 35, 106, 20);
		contentPanel.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		txtContrasena = new JPasswordField();
		txtContrasena.setBounds(123, 66, 106, 20);
		contentPanel.add(txtContrasena);
		txtContrasena.setColumns(10);
		
		lblBaseDeDatos = new JLabel("Base de Datos");
		lblBaseDeDatos.setBounds(10, 103, 103, 14);
		contentPanel.add(lblBaseDeDatos);
		
		cbCatalogo = new JComboBox();
		cbCatalogo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				listarCatalogo();
			}
		});
		cbCatalogo.setBounds(123, 97, 106, 22);
		contentPanel.add(cbCatalogo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						aceptar();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelar();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
