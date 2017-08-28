package org.neos.parser;

import java.io.IOException;

import javax.swing.JPanel;

import org.w3c.dom.Node;

public abstract class AComponente {
protected String token;
	
	public AComponente(Node node) {
		this.token = node.getNodeName();
	}

	public abstract JPanel construirPanel();
	
	public abstract String getConteudoComponente() throws IOException;
	
	public String getToken() {
		return token;
	}
}
