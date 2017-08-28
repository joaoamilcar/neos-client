package org.neos.cluster;

public abstract class ManipuladorDeCluster {
	protected Dataset dataset;
	
	public ManipuladorDeCluster(Dataset dataset) {
		setDataset(dataset);
	}
	
	public void templateMapearCluster() {
		for (Cluster cluster : dataset.getListaClusters()) {
			mapearPontos(cluster);
			mapearLigacoes(cluster);
		}
	}
	
	protected abstract void mapearPontos(Cluster cluster);
	
	protected abstract void mapearLigacoes(Cluster cluster);
	
	protected void ligarPontoCentral(Ponto pontoCentral, Cluster cluster) { // proíbe ligação de um ponto central para ele mesmo
		for (Ponto ponto : cluster.getPontos()) {
			if (!ponto.equals(pontoCentral)) {
				Ligacao ligacaoCluster = new Ligacao(pontoCentral, ponto);
				cluster.adicionarLigacao(ligacaoCluster);
			}
		}
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
}