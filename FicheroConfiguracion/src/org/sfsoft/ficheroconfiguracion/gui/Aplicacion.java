package org.sfsoft.ficheroconfiguracion.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Aplicación que muestra cómo trabajar con ficheros de Propiedades
 * @author Santiago Faci
 *
 */
public class Aplicacion {

	private JFrame frmAplicacion;
	private JMenuBar menuBar;
	private JMenu mnFichero;
	private JMenu mnHerramientas;
	private JMenu mnAyuda;
	private JMenuItem mntmPreferencias;
	
	private void mostrarConfiguracion() {
		
		Configuracion configuracion = new Configuracion();
		configuracion.mostrarDialogo();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Aplicacion window = new Aplicacion();
					window.frmAplicacion.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Aplicacion() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAplicacion = new JFrame();
		frmAplicacion.setTitle("Aplicacion");
		frmAplicacion.setBounds(100, 100, 450, 300);
		frmAplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAplicacion.setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		frmAplicacion.setJMenuBar(menuBar);
		
		mnFichero = new JMenu("Fichero");
		menuBar.add(mnFichero);
		
		mnHerramientas = new JMenu("Herramientas");
		menuBar.add(mnHerramientas);
		
		mntmPreferencias = new JMenuItem("Preferencias");
		mntmPreferencias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarConfiguracion();
			}
		});
		mnHerramientas.add(mntmPreferencias);
		
		mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
	}

}
