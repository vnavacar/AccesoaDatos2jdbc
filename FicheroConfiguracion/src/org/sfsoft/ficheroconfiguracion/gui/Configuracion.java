package org.sfsoft.ficheroconfiguracion.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

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

/**
 * Diálogo de ejemplo para mantener la configuración
 * de una aplicación
 * @author Santiago Faci
 *
 */
public class Configuracion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfServidor;
	private JTextField tfBaseDatos;
	private JTextField tfPuerto;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JCheckBox checkConectar;
	private JLabel lblNewLabel_3;

	/**
	 * Muestra el diálogo en pantalla
	 */
	public void mostrarDialogo() {
		
		// Centra la ventana
		setLocationRelativeTo(null);
		cargarConfiguracion();
		setVisible(true);
	}
	
	/**
	 * Carga la configuración del fichero de configuración (si lo hay)
	 */
	private void cargarConfiguracion() {
		
		Properties configuracion = new Properties();
		try {
			configuracion.load(new FileInputStream("configuracion"));
			tfServidor.setText(configuracion.getProperty("servidor"));
			tfBaseDatos.setText(configuracion.getProperty("basedatos"));
			tfPuerto.setText(configuracion.getProperty("puerto"));
			checkConectar.setSelected(Boolean.parseBoolean(configuracion.getProperty("conectar")));
			
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
		configuracion.setProperty("servidor", tfServidor.getText());
		configuracion.setProperty("basedatos", tfBaseDatos.getText());
		configuracion.setProperty("puerto", tfPuerto.getText());
		configuracion.setProperty("conectar", String.valueOf(checkConectar.isSelected()));
		
		try {
			configuracion.store(new FileOutputStream("configuracion"), "-- Ejemplo de fichero de propiedades --");
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
	public Configuracion() {
		setModal(true);
		setTitle("Configuracion");
		setBounds(100, 100, 271, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		tfServidor = new JTextField();
		tfServidor.setBounds(99, 66, 134, 28);
		contentPanel.add(tfServidor);
		tfServidor.setColumns(10);
		
		tfBaseDatos = new JTextField();
		tfBaseDatos.setBounds(99, 106, 134, 28);
		contentPanel.add(tfBaseDatos);
		tfBaseDatos.setColumns(10);
		
		tfPuerto = new JTextField();
		tfPuerto.setBounds(99, 146, 56, 28);
		contentPanel.add(tfPuerto);
		tfPuerto.setColumns(10);
		
		lblNewLabel = new JLabel("Servidor");
		lblNewLabel.setBounds(6, 72, 93, 16);
		contentPanel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Base de Datos");
		lblNewLabel_1.setBounds(6, 112, 93, 16);
		contentPanel.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Puerto");
		lblNewLabel_2.setBounds(6, 152, 93, 16);
		contentPanel.add(lblNewLabel_2);
		
		checkConectar = new JCheckBox("Conectar al inicio");
		checkConectar.setBounds(99, 187, 196, 23);
		contentPanel.add(checkConectar);
		
		lblNewLabel_3 = new JLabel("Preferencias");
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblNewLabel_3.setBounds(82, 21, 183, 16);
		contentPanel.add(lblNewLabel_3);
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
}
