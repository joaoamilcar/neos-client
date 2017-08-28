package org.neos.parser;

import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.w3c.dom.Node;

public class ComponenteTexto extends AComponente {
	private JTextArea areaTexto;
	
	public ComponenteTexto(Node node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JPanel construirPanel() {
		JPanel panel = new JPanel();
		areaTexto = new JTextArea(1, 40);
		panel.add(areaTexto);
		
		return panel;
	}

	@Override
	public String getConteudoComponente() throws IOException {
		return areaTexto.getText();
	}
}
