package org.sfsoft.holasql.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JRadioButton;

/**
 * Diálogo de ejemplo para mantener la configuración
 * de una aplicación
 * @author Santiago Faci
 *
 */
public class JConfiguracion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfServidor;
	private JTextField tfBaseDatos;
	private JTextField tfPuerto;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;
	private JTextField tfUsuario;
	private JTextField tfContrasena;
	private JLabel lblUsuario;
	private JLabel lblPassword;
	public JRadioButton rbMysql;
	public JRadioButton rbPostgresql;

	/**
	 * Muestra el diálogo en pantalla
	 */
	public void mostrarDialogo() {
		
		// Centra la ventana
		setLocationRelativeTo(null);
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rbMysql);
		radioGroup.add(rbPostgresql);
		
		cargarConfiguracion();
		setVisible(true);
	}
	
	/**
	 * Carga la configuración del fichero de configuración (si lo hay)
	 */
	private void cargarConfiguracion() {
		
		Properties configuracion = new Properties();
		try {
			configuracion.load(new FileInputStream("configuracion.props"));
			String driver = configuracion.getProperty("driver");
			if (driver.equals("com.mysql.jdbc.Driver"))
				rbMysql.setSelected(true);
			else
				rbPostgresql.setSelected(true);
			
			tfServidor.setText(configuracion.getProperty("servidor"));
			tfBaseDatos.setText(configuracion.getProperty("basedatos"));
			tfPuerto.setText(configuracion.getProperty("puerto"));
			tfUsuario.setText(configuracion.getProperty("usuario"));
			tfContrasena.setText(configuracion.getProperty("contrasena"));
			
		} catch (FileNotFoundException e) {
			// TODO Mostrar mensaje de error
		} catch (IOException e) {
			// TODO Mostrar mensaje de error
		}
	}
	
	/**
	 * Guarda la configuración y cierra la ventana
	 */
	private void aceptar() {
		
		Properties configuracion = new Properties();
		if (rbMysql.isSelected()) {
			configuracion.setProperty("driver", "com.mysql.jdbc.Driver");
			configuracion.setProperty("protocolo", "jdbc:mysql://");
		}
		else {
			configuracion.setProperty("driver", "org.postgresql.Driver");
			configuracion.setProperty("protocolo", "jdbc:postgresql://");
		}
		configuracion.setProperty("servidor", tfServidor.getText());
		configuracion.setProperty("basedatos", tfBaseDatos.getText());
		configuracion.setProperty("puerto", tfPuerto.getText());
		configuracion.setProperty("usuario", tfUsuario.getText());
		configuracion.setProperty("contrasena", tfContrasena.getText());
		
		try {
			configuracion.store(new FileOutputStream("configuracion.props"), "-- Ejemplo de fichero de propiedades --");
			setVisible(false);
		} catch (FileNotFoundException fnfe) {
			// TODO Mostrar mensaje de error
		} catch (IOException ioe) {
			// TODO Mostrar mensaje de error
		}
	}
	
	/**
	 * Cierra la ventana sin guardar los cambios
	 */
	private void cancelar() {
		setVisible(false);
	}

	/**
	 * Create the dialog.
	 */
	public JConfiguracion() {
		setModal(true);
		setTitle("Configuracion");
		setBounds(100, 100, 321, 348);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		tfServidor = new JTextField();
		tfServidor.setBounds(103, 106, 134, 28);
		contentPanel.add(tfServidor);
		tfServidor.setColumns(10);
		
		tfBaseDatos = new JTextField();
		tfBaseDatos.setBounds(103, 146, 134, 28);
		contentPanel.add(tfBaseDatos);
		tfBaseDatos.setColumns(10);
		
		tfPuerto = new JTextField();
		tfPuerto.setBounds(251, 106, 56, 28);
		contentPanel.add(tfPuerto);
		tfPuerto.setColumns(10);
		
		lblNewLabel = new JLabel("Servidor/Puerto");
		lblNewLabel.setBounds(10, 112, 93, 16);
		contentPanel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Base de Datos");
		lblNewLabel_1.setBounds(10, 152, 93, 16);
		contentPanel.add(lblNewLabel_1);
		
		lblNewLabel_3 = new JLabel("Preferencias");
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblNewLabel_3.setBounds(82, 21, 183, 16);
		contentPanel.add(lblNewLabel_3);
		contentPanel.add(getTfUsuario());
		contentPanel.add(getTfContrasena());
		contentPanel.add(getLblUsuario());
		contentPanel.add(getLblPassword());
		{
			rbMysql = new JRadioButton("MySQL");
			rbMysql.setBounds(20, 61, 109, 23);
			contentPanel.add(rbMysql);
		}
		{
			rbPostgresql = new JRadioButton("PostgreSQL");
			rbPostgresql.setBounds(158, 61, 109, 23);
			contentPanel.add(rbPostgresql);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
	public JTextField getTfUsuario() {
		if (tfUsuario == null) {
			tfUsuario = new JTextField();
			tfUsuario.setColumns(10);
			tfUsuario.setBounds(103, 185, 134, 28);
		}
		return tfUsuario;
	}
	public JTextField getTfContrasena() {
		if (tfContrasena == null) {
			tfContrasena = new JTextField();
			tfContrasena.setColumns(10);
			tfContrasena.setBounds(103, 227, 134, 28);
		}
		return tfContrasena;
	}
	public JLabel getLblUsuario() {
		if (lblUsuario == null) {
			lblUsuario = new JLabel("Usuario");
			lblUsuario.setBounds(10, 192, 93, 16);
		}
		return lblUsuario;
	}
	public JLabel getLblPassword() {
		if (lblPassword == null) {
			lblPassword = new JLabel("Password");
			lblPassword.setBounds(10, 234, 93, 16);
		}
		return lblPassword;
	}
}
