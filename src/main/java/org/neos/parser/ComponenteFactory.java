package org.neos.parser;

import org.w3c.dom.Node;

public class ComponenteFactory {
	public AComponente criarComponenteGUI(Node node) {
		String tagNome = node.getNodeName();
		
		if (tagNome.equals(tagNome.toUpperCase()) || tagNome.equals("comment") || tagNome.equals("comments")
				|| tagNome.equals("num1") || tagNome.equals("num2") || tagNome.equals("operation") || tagNome.equals("email")) {
			return new ComponenteTexto(node);
		} else {
			return new ComponenteArquivo(node);
		}
	}
}
