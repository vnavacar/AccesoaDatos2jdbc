package org.sfsoft.transaccionessql.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que muestra el contenido de una tabla de MySQL en un JTable
 * Se han añadido métodos que permiten registrar, modificar y eliminar contenido de la Base de Datos
 * @author Santiago Faci
 * @version 2.0
 */
public class JTablaDatos extends JTable {
	
	// Conexión con la Base de Datos
	private Connection conexion;
	private DefaultTableModel modeloDatos;
	
	// Constantes para definir el nombre de la tabla y de las columnas de ésta
	private static final String TABLA = "enemigos";
	private static final String NOMBRE = "nombre";
	private static final String ENERGIA = "energia";
	private static final String DANO = "dano";
	private static final String ENEMIGO_PANTALLA = "enemigo_pantalla";
	private static final String ID_ENEMIGO = "id_enemigo";
	private static final String ID_PANTALLA = "id_pantalla";
	
	// Indica si estamos trabajando en modo depuración
	private static final boolean DEBUG = false; 
	
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
		
		modeloDatos.addColumn("nombre");
		modeloDatos.addColumn("energía");
		modeloDatos.addColumn("daño");
		
		this.setModel(modeloDatos);
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
	}

	/*
	 * 'Pinta' los datos en el JTable
	 */
	private void cargarFilas(ResultSet resultado) throws SQLException {
	
		modeloDatos.setNumRows(0);
		
		while (resultado.next()) {
			Object[] fila = new Object[]{resultado.getString(2), String.valueOf(resultado.getInt(3)), 
					String.valueOf(resultado.getInt(4))};
			modeloDatos.addRow(fila);
		}
	}
	
	/**
	 * Elimina todo el contenido del control JTable
	 */
	public void vaciar() {
		
		modeloDatos.setNumRows(0);
	}
	
	/**
	 * Registra un nuevo enemigo en la Base de Datos y las pantallas que se le hayan asignado
	 * Lo ejecuta como una transacción puesto que esta tarea requiere realizar varias operaciones
	 * @param nombre
	 * @param energia
	 * @param dano
	 * @param pantallas
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void nuevo(String nombre, int energia, int dano, ArrayList<Integer> pantallas) throws SQLException {
		
		// No se validará ninguna operación contra la Base de Datos hasta que no se invoque a 'commit'
		conexion.setAutoCommit(false);
		
		String insertarEnemigoSql = "INSERT INTO " + TABLA + " ("  + NOMBRE + ", "
				+ ENERGIA + ", " + DANO + ") VALUES (?, ?, ?)";
		String asignarPantallasSql = "INSERT INTO " + ENEMIGO_PANTALLA + " VALUES (?, ?)";
		
		// Primero se registra el enemigo
		PreparedStatement insertarEnemigo = conexion.prepareStatement(insertarEnemigoSql, 
				PreparedStatement.RETURN_GENERATED_KEYS);
		insertarEnemigo.setString(1, nombre);
		insertarEnemigo.setInt(2, energia);
		insertarEnemigo.setInt(3, dano);
		insertarEnemigo.executeUpdate();
		
		// Obtiene el id generado al registrar el nuevo enemigo
		ResultSet idsGenerados = insertarEnemigo.getGeneratedKeys();
		idsGenerados.next();
		int idEnemigo = idsGenerados.getInt(1);
		idsGenerados.close();
		insertarEnemigo.close();
		
		// Asigna las pantallas al enemigo que se acaba de registrar
		PreparedStatement asignarPantallas = conexion.prepareStatement(asignarPantallasSql);
		for (Integer idPantalla : pantallas) {
			asignarPantallas.setInt(1, idEnemigo);
			asignarPantallas.setInt(2, idPantalla);
			asignarPantallas.executeUpdate();
		}
		
		// Valida todas las operaciones realizadas
		conexion.commit();
		
		// Libera los recursos ocupados por las sentencias
		if (insertarEnemigo != null)
			insertarEnemigo.close();
		if (asignarPantallas != null)
			asignarPantallas.close();
	}
	
	/**
	 * Modifica los datos de un enemigo en la Base de Datos
	 * @param nombre
	 * @param energia
	 * @param dano
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void modificar(String nombreOriginal, String nombre, int energia, int dano) 
			throws SQLException {
		
		String sentenciaSql = "UPDATE " + TABLA + " SET " + NOMBRE + " = ?, "
				+ ENERGIA + " = ?, "
				+ DANO + " = ? WHERE " + NOMBRE + " = ?";
		
		PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
		sentencia.setString(1, nombre);
		sentencia.setInt(2, energia);
		sentencia.setInt(3, dano);
		sentencia.setString(5, nombreOriginal);
		sentencia.executeUpdate();
	}
	
	/**
	 * Elimina, de la Base de Datos, el enemigo seleccionado
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
	}
	
	/**
	 * Comprueba si un enemigo ya está repetido en la Base de Datos (si se repite el nombre)
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
