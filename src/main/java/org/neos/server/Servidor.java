package org.neos.server;

public class Servidor {
	
	private String host, porta;
	
	public Servidor(String host, String porta) {
		setHost(host);
		setPorta(porta);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}
	
	
}
