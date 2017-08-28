package org.neos.cluster;

public class Ligacao {
	private final Ponto origem;
	private final Ponto destino;
	
	public Ligacao(Ponto aPartirDe, Ponto para) {
		this.origem = aPartirDe;
		this.destino = para;
	}

	public Ponto getOrigem() {
		return origem;
	}

	public Ponto getDestino() {
		return destino;
	}
}
