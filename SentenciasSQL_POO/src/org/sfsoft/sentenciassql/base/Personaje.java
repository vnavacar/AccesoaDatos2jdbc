package org.sfsoft.sentenciassql.base;

/**
 * Clase que representa a un personaje
 * @author Santiago Faci
 * @curso 2014-2015
 */
public class Personaje {

	private int id;
	private String nombre;
	private int nivel;
	private int energia;
	private int puntos;
	
	public Personaje() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getEnergia() {
		return energia;
	}
	public void setEnergia(int energia) {
		this.energia = energia;
	}
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
}
