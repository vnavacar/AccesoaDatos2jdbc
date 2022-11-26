package org.sfsoft.holasql.gui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 * Clase que muestra el contenido de una tabla de MySQL en un JTable
 * @author Santiago Faci
 * @version 1.0
 */
public class JTablaDatos extends JTable {
	
	private Connection conexion;
	private DefaultTableModel modeloDatos;
	
	private static final boolean DEBUG = false; 
	
	public JTablaDatos() {
		super();

		inicializar();
	}
	
	/**
	 * Inicializa la estructura de la tabla
	 */
	private void inicializar() {
		
		modeloDatos = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		
		modeloDatos.addColumn("nombre");
		modeloDatos.addColumn("nivel");
		modeloDatos.addColumn("energia");
		modeloDatos.addColumn("puntos");
		
		this.setModel(modeloDatos);
	}
	
	/**
	 * Fija la conexión con la Base de Datos
	 * @param conexion
	 */
	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}
	
	/**
	 * Muestra el contenido de la tabla de la Base de Datos
	 * @throws SQLException En caso de que haya algún fallo con la conexión
	 */
	public void listar() throws SQLException {
		
		if (conexion == null)
			return;
		
		if (conexion.isClosed())
			return;
		
		if (DEBUG)
			System.out.println("listando");
		
		String consulta = null;
		Statement sentencia = null;
		ResultSet resultado = null;
		
		sentencia = conexion.createStatement();
		consulta = "SELECT * FROM personajes";
		resultado = sentencia.executeQuery(consulta);
		
		cargarFilas(resultado);
		
		if (sentencia != null)
			sentencia.close();
	}
	
	/**
	 * Muestra los datos de la tabla aplicando un filtro
	 * @param filtro
	 * @throws SQLException En caso de que haya algún fallo con la conexión
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
		consulta = "SELECT * FROM personajes WHERE nombre LIKE '%" + filtro + "%'";
		resultado = sentencia.executeQuery(consulta);
		
		cargarFilas(resultado);
		
		if (sentencia != null)
			sentencia.close();
	}

	/*
	 * Recorre el control JTable con los datos de la tabla de la Base de Datos
	 */
	private void cargarFilas(ResultSet resultado) throws SQLException {
	
		modeloDatos.setNumRows(0);
		
		while (resultado.next()) {
			Object[] fila = new Object[]{resultado.getString(2), String.valueOf(resultado.getInt(3)), 
					String.valueOf(resultado.getInt(4)), String.valueOf(resultado.getInt(5))};
			modeloDatos.addRow(fila);
		}
	}
}
