package org.sfsoft.transaccionessql.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.sfsoft.transaccionessql.util.Constantes.AccionDialogo;

/**
 * Dialog con el que el usuario introduce información sobre un Enemigo
 * También permite asignar en que pantallas aparecerá el enemigo al mismo tiempo que se crea
 * @author Santiago Faci
 * @version 1.0
 */
public class JEnemigo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtEnergia;
	private JTextField txtDano;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblArma;
	private ComboPantalla cbPantallas;
	private JScrollPane scrollPane;
	private JList listPantallas;
	private JLabel lblNewLabel_3;
	private JButton btAsignarPantalla;
	
	private String nombre;
	private int nivel;
	private int energia;
	private int puntos;
	private ArrayList<Integer> listaIdPantallas;
	private ArrayList<String> listaNombrePantallas;
	private DefaultListModel modeloLista; 
	private AccionDialogo accion;
	
	/**
	 * Getters y setters para obtener y fijar información en la ventana
	 * @return
	 */
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.txtNombre.setText(nombre);
	}
	
	public int getEnergia() {
		return energia;
	}
	
	public void setEnergia(String energia) {
		this.txtEnergia.setText(energia);
	}
	
	public int getDano() {
		return puntos;
	}
	
	public void setDano(String dano) {
		this.txtDano.setText(dano);
	}
	
	public ArrayList<Integer> getPantallas() {
		return listaIdPantallas;
	}
	
	public void setPantallas(ArrayList<Integer> pantallas) {
		this.listaIdPantallas = pantallas;
	}
	
	public AccionDialogo mostrarDialogo() {
		setVisible(true);
		return accion;
	}
	
	public void setConexion(Connection conexion) {
		this.cbPantallas.setConexion(conexion);
		
		try {
			cbPantallas.listar();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "No se han podido cargar las pantallas del juego");
			sqle.printStackTrace();
		}
	}
	
	private void asignarPantalla() {
	
		String pantallaSeleccionada = null;
		
		pantallaSeleccionada = (String) cbPantallas.getSelectedItem();
		if (pantallaSeleccionada == null) 
			return;
		
		Object[] pantalla = pantallaSeleccionada.split("-"); 
		
		listaIdPantallas.add(Integer.parseInt((String) pantalla[0]));
		listaNombrePantallas.add(pantallaSeleccionada);
		cargarListaPantallas();
	}
	
	private void cargarListaPantallas() {
		
		modeloLista.removeAllElements();
		for (String nombrePantalla : listaNombrePantallas)
			modeloLista.addElement(nombrePantalla);
	}
	
	/**
	 * Se invoca cuando el usuario ha pulsado en Aceptar. Recoge y valida la información de las cajas de texto
	 * y cierra la ventana
	 */
	private void aceptar() {
		
		if (txtNombre.getText().equals(""))
			return;
		
		try {
			if (txtEnergia.getText().equals(""))
				txtEnergia.setText("0");
			if (txtDano.getText().equals(""))
				txtDano.setText("0");
			
			nombre = txtNombre.getText();
			nivel = Integer.parseInt(txtEnergia.getText());
			energia = Integer.parseInt(txtDano.getText());
			
			accion = AccionDialogo.ACEPTAR;
			setVisible(false);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, "Comprueba que los datos son correctos", "Enemigo", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Invocado cuando el usuario cancela. Simplemente cierra la ventana
	 */
	private void cancelar() {
		accion = AccionDialogo.CANCELAR;
		setVisible(false);
	}

	
	private void inicializar() {
		
		listaIdPantallas = new ArrayList<Integer>();
		listaNombrePantallas = new ArrayList<String>();
		modeloLista = new DefaultListModel();
		listPantallas.setModel(modeloLista);
	}
	
	/**
	 * Constructor. Crea la ventana.
	 * Aqui añado la llamada al método inicializar
	 */
	public JEnemigo() {
		setModal(true);
		setTitle("Enemigo");
		setBounds(100, 100, 361, 241);
		
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(76, 22, 123, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);
		
		txtEnergia = new JTextField();
		txtEnergia.setBounds(76, 64, 39, 20);
		contentPanel.add(txtEnergia);
		txtEnergia.setColumns(10);
		
		txtDano = new JTextField();
		txtDano.setBounds(76, 106, 39, 20);
		contentPanel.add(txtDano);
		txtDano.setColumns(10);
		
		lblNewLabel = new JLabel("Nombre");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(10, 25, 46, 14);
		contentPanel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Energ\u00EDa");
		lblNewLabel_1.setBounds(10, 67, 46, 14);
		contentPanel.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Da\u00F1o");
		lblNewLabel_2.setBounds(10, 109, 46, 14);
		contentPanel.add(lblNewLabel_2);
		
		lblArma = new JLabel("Pantalla");
		lblArma.setBounds(10, 150, 46, 14);
		contentPanel.add(lblArma);
		
		cbPantallas = new ComboPantalla();
		cbPantallas.setBounds(76, 146, 95, 22);
		contentPanel.add(cbPantallas);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(228, 84, 117, 84);
		contentPanel.add(scrollPane);
		
		listPantallas = new JList();
		scrollPane.setViewportView(listPantallas);
		
		lblNewLabel_3 = new JLabel("Pantallas");
		lblNewLabel_3.setBounds(228, 67, 60, 14);
		contentPanel.add(lblNewLabel_3);
		
		btAsignarPantalla = new JButton(">");
		btAsignarPantalla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				asignarPantalla();
			}
		});
		btAsignarPantalla.setBounds(181, 146, 46, 23);
		contentPanel.add(btAsignarPantalla);
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
		
		inicializar();
	}
}
