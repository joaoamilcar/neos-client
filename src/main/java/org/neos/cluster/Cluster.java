package org.neos.cluster;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private List<Ponto> pontos;
	private List<Ligacao> ligacoes;
	private int idCluster;
	
	public Cluster() {
		setPontos(new ArrayList<Ponto>());
		setLigacoes(new ArrayList<Ligacao>());
	}

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}
	
	public Ponto getPonto(int id) {
		return pontos.get(id);		
	}

	public List<Ligacao> getLigacoes() {
		return ligacoes;
	}

	public void setLigacoes(List<Ligacao> ligacoes) {
		this.ligacoes = ligacoes;
	}
	
	public int getTamanho() {
		return pontos.size();
	}
	
	public boolean adicionarPonto(Ponto ponto) {
		boolean jaExiste = false;
		
		if (pontos.contains(ponto) == false) {
			jaExiste = pontos.add(ponto);
		}
		
		return jaExiste;
	}
	
	public boolean adicionarLigacao(Ligacao ligacao) {
		boolean jaExiste = false;
		
		if (ligacoes.contains(ligacao) == false) {
			jaExiste = ligacoes.add(ligacao);
		}
		
		return jaExiste;
	}

	public int getIdCluster() {
		return idCluster;
	}

	public void setIdCluster(int idCluster) {
		this.idCluster = idCluster;
	}
}
