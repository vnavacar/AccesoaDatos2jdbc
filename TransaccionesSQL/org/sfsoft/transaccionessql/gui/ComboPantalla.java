package org.sfsoft.transaccionessql.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;

public class ComboPantalla extends JComboBox<String> {

	private Connection conexion;
	private final static String TABLA = "pantallas";
	
	public ComboPantalla() {
		super();
	}
	
	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}
	
	public void listar() throws SQLException {
		
		PreparedStatement sentencia = conexion.prepareCall("SELECT id, nombre FROM " + TABLA);
		ResultSet resultado = sentencia.executeQuery();
		
		removeAll();
		while (resultado.next())
			addItem((String) (resultado.getInt(1) + "-" + resultado.getString(2)));		
	}
}
