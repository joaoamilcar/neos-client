package org.neos.cluster;

public class ManipuladorDeClusterCentroid extends ManipuladorDeCluster {
	public ManipuladorDeClusterCentroid(Dataset dataset) {
		super(dataset);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void mapearPontos(Cluster cluster) {
		double x = 0, y = 0;
		double tamanhoCluster = cluster.getTamanho();
		
		if (tamanhoCluster > 1) {
			for (Ponto ponto : cluster.getPontos()) {
				x = x + ponto.getCoordX();
				y = y + ponto.getCoordY();
			}
			
			Ponto pontoEspecial = new Ponto(x / tamanhoCluster, y / tamanhoCluster, cluster.getIdCluster());
			pontoEspecial.setPontoEspecial(true);
			pontoEspecial.setId(dataset.getTamanhoDoDataset() + 1);
			cluster.adicionarPonto(pontoEspecial);
		}
	}

	@Override
	protected void mapearLigacoes(Cluster cluster) {
		for (Ponto ponto : cluster.getPontos()) {
			if (ponto.isPontoEspecial() && cluster.getTamanho() > 1) {
				ligarPontoCentral(ponto, cluster);
			}
		}
	}
}