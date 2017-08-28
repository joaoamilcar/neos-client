package org.neos.cluster;

import java.util.ArrayList;
import java.util.List;

public class Dataset {
	private List<Cluster> listaClusters;
	private String algoritmo;
	
	public Dataset(int numeroDeClusters) {
		this.listaClusters = new ArrayList<Cluster>(numeroDeClusters);
	}
	
	public boolean adicionarPonto(Ponto ponto) {
		boolean jaExiste = false;
		
		for (Cluster cluster : listaClusters) {
			if (cluster.getIdCluster() == ponto.getGrupo() && cluster.getPontos().contains(ponto) == false) {
				jaExiste = cluster.getPontos().add(ponto);
			}
		}
		
		return jaExiste;
	}
	
	public boolean adicionarCluster(Cluster cluster) {
		boolean jaExiste = false;

		if (listaClusters.contains(cluster) == false) {
			jaExiste = listaClusters.add(cluster);
		}

		return jaExiste;
	}
	
	public Cluster getCluster(int idCluster) {
		for (Cluster cluster : listaClusters) {
			if (cluster.getIdCluster() == idCluster) {
				return cluster;
			}
		}
		
		return null;
	}
	
	public int getTamanhoDoDataset() {
		int tamanho = 0;
		
		for (Cluster cluster : listaClusters) {
			tamanho = tamanho + cluster.getPontos().size(); 
		}
		
		return tamanho;
	}

	public List<Cluster> getListaClusters() {
		return listaClusters;
	}

	public void setListaClusters(List<Cluster> listaClusters) {
		this.listaClusters = listaClusters;
	}

	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
}