package org.neos.cluster;

import java.util.ArrayList;
import java.util.List;

public class Ponto {
	private int id, grupo;
	private double coordX, coordY;
	private List<Ligacao> arestas;
	private boolean pontoEspecial;
	
	public Ponto(double coordX, double coordY, int grupo) {
		this.coordX = coordX;
		this.coordY = coordY;
		this.grupo = grupo;
		setArestas(new ArrayList<Ligacao>());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGrupo() {
		return grupo;
	}

	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	public List<Ligacao> getArestas() {
		return arestas;
	}

	public void setArestas(List<Ligacao> arestas) {
		this.arestas = arestas;
	}

	public boolean isPontoEspecial() {
		return pontoEspecial;
	}

	public void setPontoEspecial(boolean pontoEspecial) {
		this.pontoEspecial = pontoEspecial;
	}
}