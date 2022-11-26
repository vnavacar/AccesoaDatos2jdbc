package org.sfsoft.sentenciassql.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que muestra el contenido de una tabla de MySQL en un JTable
 * Se han añadido métodos que permiten registrar, modificar y eliminar contenido de la Base de Datos
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class JTablaDatos extends JTable {
	
	// Conexión con la Base de Datos
	private Connection conexion;
	private DefaultTableModel modeloDatos;
	
	// Constantes para definir el nombre de la tabla y de las columnas de ésta
	private static final String TABLA = "personajes";
	private static final String NOMBRE = "nombre";
	private static final String NIVEL = "nivel";
	private static final String ENERGIA = "energia";
	private static final String PUNTOS = "puntos";

	public JTablaDatos() {
		super();

		inicializar();
	}
	
	/**
	 * Inicializa la tabla, creando las columnas
	 */
	private void inicializar() {
		
		modeloDatos = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		
		modeloDatos.addColumn(NOMBRE);
		modeloDatos.addColumn(NIVEL);
		modeloDatos.addColumn(ENERGIA);
		modeloDatos.addColumn(PUNTOS);
		
		setModel(modeloDatos);
	}
	
	/**
	 * Asigna una conexión de datos a la tabla
	 * @param conexion La conexión con MySQL
	 */
	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}
	
	/**
	 * Lista el contenido de la tabla
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void listar() throws SQLException {
		
		if (conexion == null)
			return;
		
		if (conexion.isClosed())
			return;
		
		String consulta = null;
		PreparedStatement sentencia = null;
		ResultSet resultado = null;
		
		consulta = "SELECT * FROM " + TABLA;
		sentencia = conexion.prepareStatement(consulta);
		resultado = sentencia.executeQuery();
		
		cargarFilas(resultado);
		
		if (sentencia != null)
			sentencia.close();
	}
	
	/**
	 * Lista el contenido de la tabla aplicando un filtro
	 * @param filtro 
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void listar(String filtro) throws SQLException {
		
		if (conexion == null)
			return;
		
		if (conexion.isClosed())
			return;
		
		String consulta = null;
		Statement sentencia = null;
		ResultSet resultado = null;
		
		sentencia = conexion.createStatement();
		consulta = "SELECT * FROM " + TABLA + " WHERE " + NOMBRE + " LIKE '%" + filtro + "%'";
		resultado = sentencia.executeQuery(consulta);
		
		cargarFilas(resultado);
		
		if (sentencia != null)
			sentencia.close();
	}

	/*
	 * 'Pinta' los datos en el JTable
	 */
	private void cargarFilas(ResultSet resultado) throws SQLException {
	
		modeloDatos.setNumRows(0);
		
		while (resultado.next()) {
			Object[] fila = new Object[]{resultado.getString(2), String.valueOf(resultado.getInt(3)), 
					String.valueOf(resultado.getInt(4)), String.valueOf(resultado.getInt(5))};
			modeloDatos.addRow(fila);
		}
	}
	
	/**
	 * Elimina el contenido del control JTable
	 */
	public void vaciar() {
		
		modeloDatos.setNumRows(0);
	}
	
	/**
	 * Registra un nuevo personaje en la Base de Datos
	 * @param nombre
	 * @param nivel
	 * @param energia
	 * @param puntos
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void nuevo(String nombre, int nivel, int energia, int puntos) throws SQLException {
		
		String sentenciaSql = "INSERT INTO " + TABLA + " ("  + NOMBRE + ", " + NIVEL + ", "
				+ ENERGIA + ", " + PUNTOS + ") VALUES (?, ?, ?,?)";
		PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
		sentencia.setString(1, nombre);
		sentencia.setInt(2, nivel);
		sentencia.setInt(3, energia);
		sentencia.setInt(4, puntos);
		sentencia.executeUpdate();
		
		if (sentencia != null)
			sentencia.close();
	}
	
	/**
	 * Modifica los datos de un personaje en la Base de Datos
	 * @param nombre
	 * @param nivel
	 * @param energia
	 * @param puntos
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void modificar(String nombreOriginal, String nombre, int nivel, int energia, int puntos) 
			throws SQLException {
		
		String sentenciaSql = "UPDATE " + TABLA + " SET " + NOMBRE + " = ?, "
				+ NIVEL + " = ?, " + ENERGIA + " = ?, "
				+ PUNTOS + " = ? WHERE " + NOMBRE + " = ?";
		
		PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
		sentencia.setString(1, nombre);
		sentencia.setInt(2, nivel);
		sentencia.setInt(3, energia);
		sentencia.setInt(4, puntos);
		sentencia.setString(5, nombreOriginal);
		sentencia.executeUpdate();
		
		if (sentencia != null)
			sentencia.close();
	}
	
	/**
	 * Elimina, de la Base de Datos, el personaje seleccionado
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void eliminar() throws SQLException {
		
		int filaSeleccionada = 0;
		
		filaSeleccionada = getSelectedRow();
		if (filaSeleccionada == -1) 
			return;
		
		String nombreSeleccionado = (String) getValueAt(filaSeleccionada, 0);
		String sentenciaSql = "DELETE FROM " + TABLA + " WHERE " + NOMBRE + " = ?";
		
		PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
		sentencia.setString(1, nombreSeleccionado);
		sentencia.executeUpdate();
		
		if (sentencia != null)
				sentencia.close();
	}
	
	/**
	 * Comprueba si un personaje ya está repetido en la Base de Datos (si se repite el nombre)
	 * @param nombre
	 * @return true si el personaje ya existe, false en caso contrario
	 * @throws SQLException En caso de que haya un fallo de conexión con MySQL
	 */
	public boolean existe(String nombre) throws SQLException {
		
		String consulta = "SELECT COUNT(*) FROM " + TABLA + " WHERE " + NOMBRE + " = ?";
		
		PreparedStatement sentencia = conexion.prepareStatement(consulta);
		sentencia.setString(1, nombre);
		ResultSet resultado = sentencia.executeQuery();
		
		resultado.next();
		
		if (resultado.getInt(1) == 1) 
			return true;
			
		return false;
	}
}
