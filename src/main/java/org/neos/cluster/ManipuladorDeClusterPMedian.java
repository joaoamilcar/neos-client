package org.neos.cluster;

public class ManipuladorDeClusterPMedian extends ManipuladorDeCluster {
	public ManipuladorDeClusterPMedian(Dataset dataset) {
		super(dataset);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void mapearPontos(Cluster cluster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mapearLigacoes(Cluster cluster) {
		for (Ponto ponto : cluster.getPontos()) {
			if ((ponto.getId() == cluster.getIdCluster()) && cluster.getTamanho() > 1) {
				ligarPontoCentral(ponto, cluster);
			}
		}
	}
}
