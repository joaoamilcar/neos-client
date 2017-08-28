package org.neos.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class JanelaAjuda extends JFrame {
	private JTextArea areaTexto;
	private JScrollPane areaScroll;
	
	public JanelaAjuda(String texto, String titulo) {
		areaTexto = new JTextArea(texto);
		areaTexto.setEditable(false);
		areaTexto.setBackground(getBackground());
		areaScroll = new JScrollPane(areaTexto);
		
		Container container = getContentPane(); //obtem o painel de conteudo		
		container.add(areaScroll, BorderLayout.CENTER);
		
		this.setSize(700, 600);
		//this.pack(); //gerenciador de leiaute se encarrega de ajustar o tamanho da janela
		this.setLocationRelativeTo(null);
		this.setTitle(titulo);
		this.setVisible(true);
	}
}
