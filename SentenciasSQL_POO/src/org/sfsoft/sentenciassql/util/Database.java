package org.sfsoft.sentenciassql.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.sfsoft.sentenciassql.base.Personaje;

/**
* @author Santiago Faci
*/
public class Database {
	
	private Connection conexion;
	
	public Database(Connection conexion) {
		this.conexion = conexion;
	}

	public void nuevoPersonaje(Personaje personaje) 
		throws SQLException {
		
		String sentenciaSql = "INSERT INTO " + Constantes.TABLA + 
				" ("  + Constantes.NOMBRE + ", " + 
				Constantes.NIVEL + ", "
				+ Constantes.ENERGIA + ", " + Constantes.PUNTOS + 
				") VALUES (?, ?, ?, ?)";
		PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
		sentencia.setString(1, personaje.getNombre());
		sentencia.setInt(2, personaje.getNivel());
		sentencia.setInt(3, personaje.getEnergia());
		sentencia.setInt(4, personaje.getPuntos());
		sentencia.executeUpdate();
		
		if (sentencia != null)
			sentencia.close();
	}
	
	public void modificarPersonaje(String nombreOriginal, 
		Personaje personaje) throws SQLException {
		
		String sentenciaSql = "UPDATE " + Constantes.TABLA + 
				" SET " + Constantes.NOMBRE + " = ?, "
				+ Constantes.NIVEL + " = ?, " + 
				Constantes.ENERGIA + " = ?, "
				+ Constantes.PUNTOS + " = ? WHERE " + 
				Constantes.NOMBRE + " = ?";
		
		PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
		sentencia.setString(1, personaje.getNombre());
		sentencia.setInt(2, personaje.getNivel());
		sentencia.setInt(3, personaje.getEnergia());
		sentencia.setInt(4, personaje.getPuntos());
		sentencia.setString(5, nombreOriginal);
		sentencia.executeUpdate();
		
		if (sentencia != null)
			sentencia.close();
	}
	
	public void eliminarPersonaje(String nombre) 
		throws SQLException {
		
		String sentenciaSql = "DELETE FROM " + 
				Constantes.TABLA + " WHERE " + 
				Constantes.NOMBRE + " = ?";
		
		PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
		sentencia.setString(1, nombre);
		sentencia.executeUpdate();
		
		if (sentencia != null)
				sentencia.close();
	}
	
	public ArrayList<Personaje> getPersonajes() 
		throws SQLException {
	
		ArrayList<Personaje> personajes;
		
		String consulta = null;
		PreparedStatement sentencia = null;
		ResultSet resultado = null;
		
		consulta = "SELECT * FROM " + Constantes.TABLA;
		sentencia = conexion.prepareStatement(consulta);
		resultado = sentencia.executeQuery();
		
		personajes = crearLista(resultado);
		
		if (sentencia != null)
			sentencia.close();
		
		return personajes;
	}
	
	public ArrayList<Personaje> getPersonajes(String filtro) 
			throws SQLException {
		
		ArrayList<Personaje> personajes;
		
		String consulta = null;
		PreparedStatement sentencia = null;
		ResultSet resultado = null;
		
		consulta = "SELECT * FROM " + Constantes.TABLA
			+ " WHERE nombre LIKE '%" + filtro + "%'";
		sentencia = conexion.prepareStatement(consulta);
		resultado = sentencia.executeQuery();
		
		personajes = crearLista(resultado);
		
		if (sentencia != null)
			sentencia.close();
		
		return personajes;
	}
	
	private ArrayList<Personaje> crearLista(ResultSet resultado) 
		throws SQLException {
		
		ArrayList<Personaje> personajes = 
			new ArrayList<>();
		
		Personaje personaje = null;
		while (resultado.next()) {
			
			personaje = new Personaje();
			personaje.setId(resultado.getInt(1));
			personaje.setNombre(resultado.getString(2));
			personaje.setNivel(resultado.getInt(3));
			personaje.setEnergia(resultado.getInt(4));
			personaje.setPuntos(resultado.getInt(5));
			personajes.add(personaje);
		}
		
		return personajes;
	}
	
	public boolean existePersonaje(String nombre) throws SQLException {
		
		String consulta = "SELECT COUNT(*) FROM " + 
				Constantes.TABLA + " WHERE " + 
				Constantes.NOMBRE + " = ?";
		
		PreparedStatement sentencia = conexion.prepareStatement(consulta);
		sentencia.setString(1, nombre);
		ResultSet resultado = sentencia.executeQuery();
		
		resultado.next();
		
		if (resultado.getInt(1) == 1) 
			return true;
			
		return false;
	}
	
	public Personaje getPersonaje(String nombre)
		throws SQLException {
		
		String consulta = 
				"SELECT * FROM " + Constantes.TABLA + " WHERE " + 
				Constantes.NOMBRE + " = ?";
			
		PreparedStatement sentencia = 
			conexion.prepareStatement(consulta);
		sentencia.setString(1, nombre);
		ResultSet resultado = sentencia.executeQuery();
		
		resultado.next();
		
		Personaje personaje = new Personaje();
		personaje.setId(resultado.getInt(1));
		personaje.setNombre(resultado.getString(2));
		personaje.setNivel(resultado.getInt(3));
		personaje.setEnergia(resultado.getInt(4));
		personaje.setPuntos(resultado.getInt(5));
		
		return personaje;
	}
	
	public String ejecutarFuncion(String nombre) throws SQLException {
		
		PreparedStatement sentencia = 
			conexion.prepareStatement("select " + nombre + "()");
		ResultSet resultado = sentencia.executeQuery();
		resultado.next();
		
		String valor = resultado.getString(1);
		
		resultado.close();
		
		return valor;
	}
	
	public void ejecutarProcedimiento(String nombre) 
		throws SQLException {
		
		CallableStatement procedimiento = 
			conexion.prepareCall("{ call " + nombre + "() }");
		procedimiento.execute();
	}
}
