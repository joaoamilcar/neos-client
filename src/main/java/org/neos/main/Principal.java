package org.neos.main;

import javax.swing.UIManager;

import org.neos.parser.JanelaPrincipal;

public class Principal {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		new JanelaPrincipal();
	}
}
